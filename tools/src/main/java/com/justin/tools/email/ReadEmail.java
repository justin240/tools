package com.justin.tools.email;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang3.StringUtils;

public class ReadEmail {
	
	private static final String[] 	HEADER_FILTERS 		= {"ActivityId"};
	private static final String[] 	FROM_FILTERS 		= {"justin@localhost", "test@localhost", "shervin@localhost"};
	private static final String 	MAIL_FOLDER_INBOX 	= "INBOX";
	private static final Boolean 	UNREAD_FILTER 		= Boolean.TRUE;
	private static final Boolean 	RECENT_MAIL_FILTER 	= Boolean.TRUE;
	

	public static void main(String[] args) {
		
		String host = "localhost";
		String mailStoreType = "imap";
		String username = "libin@localhost";
		String password = "libin";

		check(host, mailStoreType, username, password);
	}

	// function to make connection and get mails from server known as "Pop" server
	public static void check(String host, String storeType, final String user, final String password) {
		try {

			Store store = getMailboxStore(host, storeType, user, password);
			
			// create the folder object and open it
			Folder emailFolder = store.getFolder(MAIL_FOLDER_INBOX);

			emailFolder.open(Folder.READ_ONLY);
			
			Message[] messages = emailFolder.search(mailFilter());
			System.out.println(messages.length);

			for (int i = 0, n = messages.length; i < n; i++) {
				
				String content;
				Message message = messages[i];
				Object obj = message.getContent();
				if(obj instanceof Multipart) {
					Multipart mp = (Multipart) obj;
					BodyPart bp = mp.getBodyPart(0);
					content = bp.getContent().toString();
				}else {
					content = obj.toString();
				}

				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("To: " + message.getAllRecipients().toString());
				System.out.println("Received Date:" + message.getReceivedDate());
				System.out.println("Text: " + content);
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Store getMailboxStore(String host, String storeType, final String user, final String password)
			throws NoSuchProviderException, MessagingException {
		
		// create properties field
		Properties props = new Properties();

		// Connect to the server
		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore(storeType);
		store.connect(host, user, password);
		
		return store;
	}

	private static SearchTerm mailFilter() {
		
		SearchTerm filter =  new SearchTerm() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean match(Message msg) {
				
				try {
					
					//check header
					if(HEADER_FILTERS.length > 0 && !hasAny(msg.getMatchingHeaders(HEADER_FILTERS), HEADER_FILTERS)) {
						return false;
					}
					
					//check from address
					if(FROM_FILTERS.length > 0 && !hasAny(msg.getFrom(), FROM_FILTERS)) {
						return false;
					}
					
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				return true;
			}
		};
		
		//Unread filter
		if(UNREAD_FILTER) {
			FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			filter = new AndTerm(filter, unseenFlagTerm);
		}
		
		//Recent mail filter
		if(RECENT_MAIL_FILTER) {
			FlagTerm recentFlagTerm = new FlagTerm(new Flags(Flags.Flag.RECENT), true);
			filter = new AndTerm(filter, recentFlagTerm);
		}
				
		return filter;
	}
	
	private static boolean hasAny(Object object, String[] list) {
		
		if(list != null && list.length > 0 && object != null) {
			
			List<Object> elements = new ArrayList<Object>();
			if(object instanceof Enumeration) {
				Enumeration<?> enumObjects = (Enumeration<?>)object;
				while(enumObjects.hasMoreElements()) {
					elements.add(enumObjects.nextElement());
				}
			}else if (object instanceof Address[]) {
				for(Address addr : (Address[])object) {
					elements.add(addr);
				}
			}
			
			for(String value:list) {
				
				for(Object element : elements) {
					String elementValue = null;
					
					if(element instanceof Header) {
						elementValue = ((Header)element).getName();
					}else if(element instanceof Address) {
						elementValue = ((Address)element).toString();
						elementValue = StringUtils.defaultIfBlank(StringUtils.substringBetween("<", ">"), element.toString());
					}
					if(value.equals(elementValue)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}