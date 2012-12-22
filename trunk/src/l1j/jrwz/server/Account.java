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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.jrwz.Base64;
import l1j.jrwz.L1DatabaseFactory;
import l1j.jrwz.server.utils.SQLUtil;

/**
 * 帳號相關資訊
 */
public class Account {
    /**
     * 設定該帳號為禁止登入
     * 
     * @param login
     *            アカウント名
     */
    public static void ban(final String login) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "UPDATE accounts SET banned=1 WHERE login=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setString(1, login);
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 建立新的帳號
     * 
     * @param name
     *            帳號名稱
     * @param rawPassword
     *            明文密碼
     * @param ip
     *            連結時的 IP
     * @param host
     *            連結時的 dns 反查
     * @return Account
     */
    public static Account create(final String name, final String rawPassword,
            final String ip, final String host) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {

            Account account = new Account();
            account._name = name;
            account._password = encodePassword(rawPassword);
            account._ip = ip;
            account._host = host;
            account._banned = false;
            account._lastActive = new Timestamp(System.currentTimeMillis());

            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "INSERT INTO accounts SET login=?,password=?,lastactive=?,access_level=?,ip=?,host=?,banned=?,character_slot=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setString(1, account._name);
            pstm.setString(2, account._password);
            pstm.setTimestamp(3, account._lastActive);
            pstm.setInt(4, 0);
            pstm.setString(5, account._ip);
            pstm.setString(6, account._host);
            pstm.setInt(7, account._banned ? 1 : 0);
            pstm.setInt(8, 0);
            pstm.execute();
            _log.info("created new account for " + name);

            return account;
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } catch (UnsupportedEncodingException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return null;
    }

    /**
     * 將明文密碼加密
     * 
     * @param rawPassword
     *            明文密碼
     * @return String
     * @throws NoSuchAlgorithmException
     *             密碼使用不存在的演算法加密
     * @throws UnsupportedEncodingException
     *             文字編碼不支援
     */
    private static String encodePassword(final String rawPassword)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] buf = rawPassword.getBytes("UTF-8");
        buf = MessageDigest.getInstance("SHA").digest(buf);

        return Base64.encodeBytes(buf);
    }

    /**
     * 從資料庫中取得指定帳號的資料
     * 
     * @param name
     *            帳號名稱
     * @return Account
     */
    public static Account load(final String name) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        Account account = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "SELECT * FROM accounts WHERE login=? LIMIT 1";
            pstm = con.prepareStatement(sqlstr);
            pstm.setString(1, name);
            rs = pstm.executeQuery();
            if (!rs.next()) {
                return null;
            }
            account = new Account();
            account._name = rs.getString("login");
            account._password = rs.getString("password");
            account._lastActive = rs.getTimestamp("lastactive");
            account._accessLevel = rs.getInt("access_level");
            account._ip = rs.getString("ip");
            account._host = rs.getString("host");
            account._banned = rs.getInt("banned") == 0 ? false : true;
            account._characterSlot = rs.getInt("character_slot");

            _log.fine("account exists");
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        return account;
    }

    /**
     * 更新資料庫中角色數目
     * 
     * @param account
     *            アカウント
     */
    public static void updateCharacterSlot(final Account account) {
        Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "UPDATE accounts SET character_slot=? WHERE login=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setInt(1, account.getCharacterSlot());
            pstm.setString(2, account.getName());
            pstm.execute();
            account._characterSlot = account.getCharacterSlot();
            _log.fine("update characterslot for " + account.getName());
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /**
     * 更新最後一次登入時的日期與時間
     * 
     * @param account
     *            帳號
     */
    public static void updateLastActive(final Account account, final String ip) {
        Connection con = null;
        PreparedStatement pstm = null;
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "UPDATE accounts SET lastactive=?, ip=? WHERE login = ?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setTimestamp(1, ts);
            pstm.setString(2, ip);
            pstm.setString(3, account.getName());
            pstm.execute();
            account._lastActive = ts;
            _log.fine("update lastactive for " + account.getName());
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    /** 使用者帳號名稱 */
    private String _name;

    /** 來源IP位址 */
    private String _ip;

    /** 加密過後的密碼 */
    private String _password;

    /** 上一次登入的日期 */
    private Timestamp _lastActive;

    /** 權限(是否為GM) */
    private int _accessLevel;

    /** 來源 DNS 反解 */
    private String _host;

    /** 是否被禁止登入 (True 代表禁止). */
    private boolean _banned;

    /** 可使用的角色數目 */
    private int _characterSlot;

    /** 帳戶是否有效 (True 代表有效). */
    private boolean _isValid = false;

    /** 紀錄用 */
    private static Logger _log = Logger.getLogger(Account.class.getName());

    /**
     * 建構式
     */
    private Account() {
    }

    /**
     * 取得帳號下的角色數目
     * 
     * @return int
     */
    public int countCharacters() {
        int result = 0;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            String sqlstr = "SELECT count(*) as cnt FROM characters WHERE account_name=?";
            pstm = con.prepareStatement(sqlstr);
            pstm.setString(1, _name);
            rs = pstm.executeQuery();
            if (rs.next()) {
                result = rs.getInt("cnt");
            }
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return result;
    }

    /**
     * 取得權限
     * 
     * @return int
     */
    public int getAccessLevel() {
        return _accessLevel;
    }

    /**
     * 取得角色數目
     * 
     * @return int
     */
    public int getCharacterSlot() {
        return _characterSlot;
    }

    /**
     * 取得 DNS 反解的域名
     * 
     * @return String
     */
    public String getHost() {
        return _host;
    }

    /**
     * 取得連線時的 IP
     * 
     * @return String
     */
    public String getIp() {
        return _ip;
    }

    /**
     * 取得上次登入的時間
     */
    public Timestamp getLastActive() {
        return _lastActive;
    }

    /**
     * 取得帳號名稱
     * 
     * @return String
     */
    public String getName() {
        return _name;
    }

    /**
     * 取得是否被禁止登入
     * 
     * @return boolean
     */
    public boolean isBanned() {
        return _banned;
    }

    /**
     * 取得是否為GM (True 代表為GM).
     * 
     * @return boolean
     */
    public boolean isGameMaster() {
        return 0 < _accessLevel;
    }

    /**
     * 取得帳號使否有效 (True 為有效).
     * 
     * @return boolean
     */
    public boolean isValid() {
        return _isValid;
    }

    /**
     * 設定角色數目
     * 
     * @param i
     *            欲設定的數目
     */
    public void setCharacterSlot(int i) {
        _characterSlot = i;
    }

    /**
     * 確認輸入的密碼與資料庫中的密碼是否相同
     * 
     * @param rawPassword
     *            明文密碼
     * @return boolean
     */
    public boolean validatePassword(final String rawPassword) {
        // 認證成功後如果再度認證就判斷為失敗
        if (_isValid) {
            return false;
        }
        try {
            _isValid = _password.equals(encodePassword(rawPassword));
            if (_isValid) {
                _password = null; // 認證成功後就將記憶體中的密碼清除
            }
            return _isValid;
        } catch (Exception e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return false;
    }
}
