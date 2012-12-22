/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.jrwz.server.clientpackets;

import java.util.logging.Logger;

import l1j.jrwz.Config;
import l1j.jrwz.server.Account;
import l1j.jrwz.server.AccountAlreadyLoginException;
import l1j.jrwz.server.ClientThread;
import l1j.jrwz.server.GameServerFullException;
import l1j.jrwz.server.LoginController;
import l1j.jrwz.server.serverpackets.S_CommonNews;
import l1j.jrwz.server.serverpackets.S_LoginResult;

// Referenced classes of package l1j.jrwz.server.clientpackets:
// ClientBasePacket

/**
 * 處理要求登入的封包
 */
public class C_AuthLogin extends ClientBasePacket {

    private static final String C_AUTH_LOGIN = "[C] C_AuthLogin";
    private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());

    public C_AuthLogin(byte[] decrypt, ClientThread client) {
        super(decrypt);
        String accountName = readS().toLowerCase();
        String password = readS();

        String ip = client.getIp();
        String host = client.getHostname();

        _log.finest("Request AuthLogin from user : " + accountName);

        if (!Config.ALLOW_2PC) {
            for (ClientThread tempClient : LoginController.getInstance()
                    .getAllAccounts()) {
                if (ip.equalsIgnoreCase(tempClient.getIp())) {
                    _log.info("拒绝 2P 登入。account=" + accountName + " host="
                            + host);
                    client.sendPacket(new S_LoginResult(
                            S_LoginResult.REASON_USER_OR_PASS_WRONG));
                    return;
                }
            }
        }

        Account account = Account.load(accountName);
        if (account == null) {
            if (Config.AUTO_CREATE_ACCOUNTS) {
                account = Account.create(accountName, password, ip, host);
            } else {
                _log.warning("account missing for user " + accountName);
            }
        }
        if (account == null || !account.validatePassword(password)) {
            client.sendPacket(new S_LoginResult(
                    S_LoginResult.REASON_USER_OR_PASS_WRONG));
            return;
        }
        if (account.isBanned()) { // BANアカウント
            _log.info("禁止登入的帐号尝试登入。account=" + accountName + " host=" + host);
            client.sendPacket(new S_LoginResult(
                    S_LoginResult.REASON_USER_OR_PASS_WRONG));
            return;
        }

        try {
            LoginController.getInstance().login(client, account);
            Account.updateLastActive(account, ip); // 更新最後一次登入的時間與IP
            client.setAccount(account);
            client.sendPacket(new S_LoginResult(S_LoginResult.REASON_LOGIN_OK));
            client.sendPacket(new S_CommonNews());
        } catch (GameServerFullException e) {
            client.kick();
            _log.info("线上人数已经饱和，切断 (" + client.getHostname() + ") 的连线。");
            return;
        } catch (AccountAlreadyLoginException e) {
            client.kick();
            _log.info("同个帐号已经登入，切断 (" + client.getHostname() + ") 的连线。");
            return;
        }
    }

    @Override
    public String getType() {
        return C_AUTH_LOGIN;
    }

}
