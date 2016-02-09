package com.tjpu;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
/**
 * 收信
 */
public class ImapClient {
	public static String[] getMail() throws MessagingException {
		java.util.Properties props = new java.util.Properties();
		props.setProperty("mail.imap.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		props.setProperty("mail.imap.socketFactory.port", "993");
		Session session = Session.getDefaultInstance(props);
		Store store = session.getStore("imap");
		store.connect("smtp.sina.com.cn", "ljj", "000000");// 这里改下
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		Message[] message = folder.getMessages();
		String[] titles = new String[message.length];
		for (int i = 0, n = message.length; i < n; i++) {
			titles[i] = message[i].getSubject();
		}
		// Close connection
		folder.close(false);
		store.close();
		return titles;
	}
}
