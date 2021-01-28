package com.muc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.jivesoftware.database.DbConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author http://blog.csdn.net/zaoan_wx
*/
public class MUCDao {

	private static final Logger log = LoggerFactory.getLogger(MUCDao.class);
	public static StringBuilder EXISTS_JID = new StringBuilder();
	public static StringBuilder ROOM_MEMBERS = new StringBuilder();
	static{
		EXISTS_JID.append("select ofmucroom.serviceID, ofmucroom.name, ofmucroom.roomid ,ofmucmember.nickname from ");
		EXISTS_JID.append(" ofmucroom join ofmucmember on ofmucroom.roomID = ofmucmember.roomID and ofmucmember.jid = ? and ofmucroom.name=?");
		EXISTS_JID.append(" union ");
		EXISTS_JID.append(" select ofmucroom.serviceID, ofmucroom.name, ofmucroom.roomid ,null from ");
		EXISTS_JID.append(" ofmucroom  join ofmucaffiliation on ofmucroom.roomID = ofmucaffiliation.roomID and ofmucaffiliation.jid = ? and ofmucroom.name=?");

		ROOM_MEMBERS.append("select ofmucroom.serviceID, ofmucroom.name, ofmucroom.roomid ,ofmucmember.nickname from ");
		ROOM_MEMBERS.append(" ofmucroom join ofmucmember on ofmucroom.roomID = ofmucmember.roomID and  ofmucroom.name=?");
		ROOM_MEMBERS.append(" union ");
		ROOM_MEMBERS.append(" select ofmucroom.serviceID, ofmucroom.name, ofmucroom.roomid ,null from ");
		ROOM_MEMBERS.append(" ofmucroom  join ofmucaffiliation on ofmucroom.roomID = ofmucaffiliation.roomID and ofmucroom.name=?");

	}
	
	/**
	 * 检测JID是否已经是群成员 或者管理员
	 * @param jid
	 * @return
	 */
	public static boolean exists(String jid,String roomName) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DbConnectionManager.getConnection();
			statement = connection.prepareStatement(EXISTS_JID.toString());
			statement.setString(1, jid);
			statement.setString(2, roomName);
			statement.setString(3, jid);
			statement.setString(4, roomName);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				return true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			DbConnectionManager.closeConnection(resultSet, statement,
					connection);
		}
		log.warn("MUCDAO exists:"+jid +":"+roomName+">false");
		return false;
	}
	
	
	/**
	 * 检测房间是否有用户
	 * @param roomName
	 * @return
	 */
	public static boolean existsMember(String roomName) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DbConnectionManager.getConnection();
			statement = connection.prepareStatement(ROOM_MEMBERS.toString());
			statement.setString(1, roomName);
			statement.setString(2, roomName);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				return true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			DbConnectionManager.closeConnection(resultSet, statement,
					connection);
		}
		log.warn("MUCDAO existsMember:"+roomName+">false");
		return false;
	}
	
	/**
	 * 修改 房间加入成员的昵称
	 * @param roomid 房间id
	 * @param jid JID
	 * @param nick 昵称
	 * @return
	 */
	public static Boolean updateNick(Long roomid,String jid,String nick){
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
		    con = DbConnectionManager.getConnection();
            pstmt = con.prepareStatement("update ofmucmember set nickname = ? where roomID = ? and jid=?");
            pstmt.setString(1, nick);
            pstmt.setLong(2, roomid);
            pstmt.setString(3, jid);
            // 执行SQL
            return pstmt.executeUpdate() >= 0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("update room nick error：" + e.getMessage());
		} finally {
			DbConnectionManager.closeConnection(null, pstmt, con);
		}
		return false;
	}

}
