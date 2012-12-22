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
package l1j.jrwz.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import l1j.jrwz.server.serverpackets.S_ServerMessage;

public class LoginController {
    private static LoginController _instance;

    @SuppressWarnings("unused")
    private static Logger _log = Logger.getLogger(LoginController.class
            .getName());

    public static LoginController getInstance() {
        if (_instance == null) {
            _instance = new LoginController();
        }
        return _instance;
    }

    private final Map<String, ClientThread> _accounts = new ConcurrentHashMap<String, ClientThread>();

    private int _maxAllowedOnlinePlayers;

    private LoginController() {
    }

    public ClientThread[] getAllAccounts() {
        return _accounts.values().toArray(new ClientThread[_accounts.size()]);
    }

    public int getMaxAllowedOnlinePlayers() {
        return _maxAllowedOnlinePlayers;
    }

    public int getOnlinePlayerCount() {
        return _accounts.size();
    }

    private void kickClient(final ClientThread client) {
        if (client == null) {
            return;
        }

        GeneralThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (client.getActiveChar() != null) {
                    client.getActiveChar()
                            .sendPackets(new S_ServerMessage(357));
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                client.kick();
            }
        });
    }

    public synchronized void login(ClientThread client, Account account)
            throws GameServerFullException, AccountAlreadyLoginException {
        if (!account.isValid()) {
            // 密碼驗證未指定或不驗證帳戶。
            // 此代碼只存在的錯誤檢測。
            throw new IllegalArgumentException("账户没有通过验证。");
        }
        if ((getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount())
                && !account.isGameMaster()) {
            throw new GameServerFullException();
        }
        if (_accounts.containsKey(account.getName())) {
            kickClient(_accounts.remove(account.getName()));
            throw new AccountAlreadyLoginException();
        }

        _accounts.put(account.getName(), client);
    }

    /**
     * 登出.
     * 
     * @param client
     *            - 客户端对象
     */
    public synchronized boolean logout(ClientThread client) {
        // by ssississi
        if (client.getAccount() == null) {
            return false;
        }
        if (_accounts.remove(client.getAccount().getName()) != null) {
            client.setAccount(null);
            return true;
        } else {
            client.setAccount(null);
            return false;
        }
    }

    public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
        _maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
    }
}
