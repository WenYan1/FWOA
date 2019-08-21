package weaver.general;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.activation.DataHandler;
import javax.activation.FileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import sun.misc.BASE64Encoder;
import weaver.conn.RecordSet;
import weaver.email.Email_Autherticator;
import weaver.email.FileDataSource;
import weaver.email.MailErrorBean;
import weaver.hrm.User;
import weaver.system.SystemComInfo;

public class SendMail extends MailErrorBean
{
  public static String IMAGE_FLAG = "<img alt=\"docimages_";
  private boolean isDebug = false;

  private SystemComInfo cominfo = new SystemComInfo();
  private String username = this.cominfo.getDefmailuser();
  private String password = this.cominfo.getDefmailpassword();
  private String mailserver = this.cominfo.getEmailserver();
  private boolean needauthsend = this.cominfo.getDebugmode().equals("1");

  private String needSSL = this.cominfo.getNeedSSL();

  private String smtpServerPort = this.cominfo.getSmtpServerPort();
  private String needReceipt = "";
  private String accountName = "";

  public String getSmtpServerPort() {
    return this.smtpServerPort;
  }

  public void setSmtpServerPort(String paramString) {
    this.smtpServerPort = paramString;
  }

  public String getNeedSSL() {
    return this.needSSL;
  }

  public void setNeedSSL(String paramString) {
    this.needSSL = paramString;
  }

  public void setNeedauthsend(boolean paramBoolean) {
    this.needauthsend = paramBoolean;
  }

  public void setUsername(String paramString) {
    this.username = paramString;
  }

  public void setPassword(String paramString) {
    this.password = paramString;
  }

  public void setMailServer(String paramString) {
    this.mailserver = paramString;
  }

  public String getNeedReceipt() {
    return this.needReceipt;
  }

  public void setNeedReceipt(String paramString) {
    this.needReceipt = paramString;
  }

  public String getAccountName() {
    return this.accountName;
  }

  public void setAccountName(String paramString) {
    this.accountName = paramString;
  }

  public boolean send(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    Session localSession = null;

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);

    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2, true));

      if (paramString3 != null) {
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      }
      if (paramString4 != null) {
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      }
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setText(paramString6);
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      Transport.send(localMimeMessage);

      return true;
    } catch (Exception localException) {
      writeLog(localException);
    }return false;
  }

  public boolean sendhtmlProxy(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    char c = Util.getSeparator();
    String str = "";
    str = paramInt + "" + c + paramString1 + c + paramString2 + c + paramString3 + c + "0" + c + paramString4;
    RecordSet localRecordSet = new RecordSet();
    localRecordSet.executeProc("MailSendRecord_Insert", str);
    return true;
  }

  public int sendhtmlMain(String paramString1, String paramString2, String paramString3, int paramInt, String paramString4, User paramUser, String paramString5, String paramString6, String paramString7)
  {
    char c = Util.getSeparator();
    String str = "";
    RecordSet localRecordSet = new RecordSet();
    int i = 0;
    localRecordSet.executeProc("SequenceIndex_SMailSendId", "");
    if (localRecordSet.next()) {
      i = localRecordSet.getInt(1);
    }
    str = i + "" + c + paramString1 + c + paramString2 + c + paramString3 + c + "" + paramInt + c + paramString4 + c + paramString5 + c + paramString6 + c + "0" + c + paramString7 + c + "" + paramUser.getUID();
    localRecordSet.executeProc("MailSendMain_Insert", str);

    return i;
  }

  public boolean sendhtml(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt, String paramString7)
  {
    Session localSession = null;

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);
    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2));
      if (paramString3 != null)
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      if (paramString4 != null)
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      String str1 = null;
      switch (paramInt)
      {
      case 1:
        str1 = "iso-8859-1";
        break;
      case 2:
        str1 = "big5";
        break;
      case 3:
        str1 = "UTF-8";
      }

      processBodyImg(paramString6);
      int i = paramString6.indexOf(IMAGE_FLAG);
      ArrayList localArrayList = new ArrayList();

      while (i != -1) {
        i = paramString6.indexOf("src=\"", i + 20);
        int j = paramString6.indexOf("\"", i);
        int k = paramString6.indexOf("\"", j + 1);
        localObject = paramString6.substring(j + 1, k);

        String str2 = "cid:img" + localArrayList.size() + "@www.weaver.com.cn";
        paramString6 = Util.StringReplace(paramString6, (String)localObject, str2);

        i = paramString6.indexOf(IMAGE_FLAG, j + str2.length());
        localArrayList.add(localObject);
      }

      processBodyImg(paramString6);

      MimeMultipart localMimeMultipart = new MimeMultipart();

      MimeBodyPart localMimeBodyPart = new MimeBodyPart();

      localMimeBodyPart.setContent(paramString6, "text/html;  charset=\"" + str1 + "\"");
      localMimeBodyPart.addHeader("Content-Transfer-Encoding", "base64");
      Object localObject = new BASE64Encoder();
      paramString6 = ((BASE64Encoder)localObject).encode(paramString6.getBytes());

      localMimeMultipart.setSubType("related");
      localMimeMultipart.addBodyPart(localMimeBodyPart);

      for (int m = 0; m < localArrayList.size(); m++)
      {
        localMimeBodyPart = new MimeBodyPart();
        localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource((String)localArrayList.get(m))));

        localMimeBodyPart.setHeader("Content-ID", "<img" + m + "@www.weaver.com.cn>");

        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.saveChanges();

      Transport.send(localMimeMessage);

      return true;
    } catch (Exception localException) {
      System.out.println("SendHtml:" + localException);
      writeLog(localException);
    }return false;
  }

  public boolean sendhtml(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt1, String paramString7, Hashtable paramHashtable, int paramInt2)
  {
    File localFile = null;
    Session localSession = null;
    RecordSet localRecordSet = new RecordSet();

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);

    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2));
      if (paramString3 != null)
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      if (paramString4 != null)
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      String str1 = null;
      switch (paramInt1)
      {
      case 1:
        str1 = "iso-8859-1";
        break;
      case 2:
        str1 = "big5";
        break;
      case 3:
        str1 = "UTF-8";
      }

      processBodyImg(paramString6);
      int i = paramString6.indexOf(IMAGE_FLAG);
      ArrayList localArrayList1 = new ArrayList();
      String str2 = "0";
      int j = 0;
      int k;
      while (i != -1) {
        j = i + IMAGE_FLAG.length();
        i = paramString6.indexOf("\"", j);
        str2 = paramString6.substring(j, i);
        i = paramString6.indexOf("src=", i + str2.length());

        k = paramString6.indexOf("\"", i);
        int m = paramString6.indexOf("\"", k + 1);
        localObject1 = paramString6.substring(k + 1, m);
        localObject2 = "cid:img" + str2 + "@www.weaver.com.cn";

        if ((((String)localObject1).indexOf("weaver.email.FileDownloadLocation") != -1) || (((String)localObject1).indexOf("weaver.file.FileDownload") != -1)) {
          i = paramString6.indexOf(IMAGE_FLAG, k + ((String)localObject1).length());
          continue;
        }
        paramString6 = Util.StringReplace(paramString6, (String)localObject1, (String)localObject2);
        i = paramString6.indexOf(IMAGE_FLAG, k + ((String)localObject2).length());
        localArrayList1.add(str2);
      }

      processBodyImg(paramString6);

      if (paramInt2 > 0) {
        k = Util.getSeparator();
        localRecordSet.executeProc("MailResourceContentUpdate", "" + paramInt2 + k + paramString6);
      }

      ArrayList localArrayList2 = new ArrayList();

      Object localObject2 = new Perl5Compiler();
      Object localObject1 = new Perl5Matcher();

      Pattern localPattern = ((PatternCompiler)localObject2).compile("<img.*?src=['\"\\s]?(/.*?weaver.email.FileDownloadLocation\\?fileid=(\\d*)).*?>", 1);

      PatternMatcherInput localPatternMatcherInput = new PatternMatcherInput(paramString6);
      int n = localArrayList1.size();
      String str3 = "";
      String str4 = "";
      MatchResult localMatchResult;
      while (((PatternMatcher)localObject1).contains(localPatternMatcherInput, localPattern)) {
        localMatchResult = ((PatternMatcher)localObject1).getMatch();
        paramString6 = Util.StringReplace(paramString6, localMatchResult.group(1), "cid:img" + n + "@www.weaver.com.cn");
        if (this.isDebug) System.out.println("list2.group1:" + localMatchResult.group(1));
        localArrayList2.add(localMatchResult.group(2));
        n++;
      }

      processBodyImg(paramString6);

      ArrayList localArrayList3 = new ArrayList();
      localPatternMatcherInput = new PatternMatcherInput(paramString6);
      localPattern = ((PatternCompiler)localObject2).compile("<img.*?src=['\"\\s]?(/.*?weaver.file.FileDownload\\?fileid=(\\d*)).*?>", 1);
      while (((PatternMatcher)localObject1).contains(localPatternMatcherInput, localPattern)) {
        localMatchResult = ((PatternMatcher)localObject1).getMatch();
        paramString6 = Util.StringReplace(paramString6, localMatchResult.group(1), "cid:img" + n + "@www.weaver.com.cn");
        if (this.isDebug) System.out.println("list3.group1:" + localMatchResult.group(1));
        localArrayList3.add(localMatchResult.group(2));
        n++;
      }

      if (this.isDebug) {
        System.out.println("LN:421,list.size:" + localArrayList1.size());
        System.out.println("LN:423,list2.size:" + localArrayList2.size());
        System.out.println("LN:424,list3.size:" + localArrayList3.size());
      }
      processBodyImg(paramString6);

      MimeMultipart localMimeMultipart = new MimeMultipart();

      MimeBodyPart localMimeBodyPart = new MimeBodyPart();

      localMimeBodyPart.setContent(paramString6, "text/html;  charset=\"" + str1 + "\"");
      localMimeBodyPart.addHeader("Content-Transfer-Encoding", "base64");
      BASE64Encoder localBASE64Encoder = new BASE64Encoder();
      paramString6 = localBASE64Encoder.encode(paramString6.getBytes());
      localMimeMultipart.setSubType("related");
      localMimeMultipart.addBodyPart(localMimeBodyPart);
      String str6;
      for (int i1 = 0; i1 < localArrayList1.size(); i1++) {
        try
        {
          Object localObject3 = paramHashtable.get(localArrayList1.get(i1));

          str6 = localObject3 != null ? (String)localObject3 : (String)paramHashtable.get(String.valueOf(i1));

          if (this.isDebug) System.out.println("imgfileRealPath:" + str6);

          localFile = new File(str6);

          String str7 = str6.substring(str6.lastIndexOf("\\") + 1);

          String str8 = (String)localArrayList1.get(i1);

          localMimeBodyPart = new MimeBodyPart();
          localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str8)));

          localMimeBodyPart.setHeader("Content-Type", "image/gif");
          localMimeBodyPart.setHeader("Content-ID", "<img" + localArrayList1.get(i1) + "@www.weaver.com.cn>");

          localMimeMultipart.addBodyPart(localMimeBodyPart);

          String str9 = "INSERT INTO MailResourceFile (mailid,filename,attachfile,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize) VALUES (" + paramInt2 + ",'" + str7 + "',null,'image/gif','" + str6 + "','0','0','0','img" + localArrayList1.get(i1) + "@www.weaver.com.cn','0',0)";
          localRecordSet.executeSql(str9);
        } catch (NullPointerException localNullPointerException1) {
          System.out.println("SendHtml>docimage:" + localNullPointerException1);
          writeLog(localNullPointerException1);
        }
      }

      n = localArrayList1.size();
      for (i1 = 0; i1 < localArrayList2.size(); i1++) {
        try {
          str3 = (String)localArrayList2.get(i1);
          localRecordSet.executeSql("SELECT isaesencrypt,aescode,filerealpath FROM MailResourceFile WHERE id=" + str3 + "");
          localRecordSet.next();
          str4 = localRecordSet.getString("filerealpath");
          String str5 = localRecordSet.getString("isaesencrypt");
          str6 = localRecordSet.getString("aescode");

          localFile = new File(str4);
          if (!localFile.exists())
            continue;
          localMimeBodyPart = new MimeBodyPart();
          localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str4, str5, str6)));
          localMimeBodyPart.setHeader("Content-Type", "image/gif");
          localMimeBodyPart.setHeader("Content-ID", "<img" + n++ + "@www.weaver.com.cn>");
          localMimeMultipart.addBodyPart(localMimeBodyPart);
        } catch (NullPointerException localNullPointerException2) {
          System.out.println("SendHtml>docimage:" + localNullPointerException2);
          writeLog(localNullPointerException2);
        }

      }

      for (i1 = 0; i1 < localArrayList3.size(); i1++) {
        try {
          str3 = (String)localArrayList3.get(i1);

          localMimeBodyPart = new MimeBodyPart();
          localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str3)));
          localMimeBodyPart.setHeader("Content-Type", "image/gif");
          localMimeBodyPart.setHeader("Content-ID", "<img" + n++ + "@www.weaver.com.cn>");
          localMimeMultipart.addBodyPart(localMimeBodyPart);
        } catch (NullPointerException localNullPointerException3) {
          System.out.println("SendHtml>docimage:" + localNullPointerException3);
          writeLog(localNullPointerException3);
        }

      }

      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.saveChanges();

      Transport.send(localMimeMessage);

      return true;
    } catch (Exception localException) {
      System.out.println("SendHtml:" + localException);
      writeLog(localException);
    }return false;
  }

  public Session setSSLConnectMsg(Session paramSession, Properties paramProperties)
  {
    paramProperties.setProperty("mail.smtp.port", this.smtpServerPort);
    paramProperties.put("mail.smtp.host", this.mailserver);
    paramProperties.put("mail.transport.protocol", "smtp");
    Email_Autherticator localEmail_Autherticator = null;

    if (this.needauthsend) {
      paramProperties.put("mail.smtp.auth", "true");
      localEmail_Autherticator = new Email_Autherticator(this.username, this.password);
    }
    if (!this.needSSL.equals("1")) {
      paramSession = Session.getInstance(paramProperties, localEmail_Autherticator);
    } else {
      Security.addProvider(new Provider());

      paramProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      paramProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
      paramProperties.setProperty("mail.smtp.socketFactory.port", this.smtpServerPort);
      paramProperties.put("mail.smtp.auth", "true");

      paramSession = Session.getInstance(paramProperties, new SendMail.1(this));
    }

    return paramSession;
  }

  public boolean sendMiltipartHtml(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt, ArrayList paramArrayList1, ArrayList paramArrayList2, String paramString7)
  {
    Session localSession = null;

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);

    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2, true));
      if (paramString3 != null)
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      if (paramString4 != null)
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      String str1 = null;
      switch (paramInt)
      {
      case 1:
        str1 = "iso-8859-1";
        break;
      case 2:
        str1 = "big5";
        break;
      case 3:
        str1 = "UTF-8";
      }

      processBodyImg(paramString6);
      int i = paramString6.indexOf(IMAGE_FLAG);
      ArrayList localArrayList = new ArrayList();

      while (i != -1) {
        i = paramString6.indexOf("src=\"", i + 20);
        int j = paramString6.indexOf("\"", i);
        int k = paramString6.indexOf("\"", j + 1);
        localObject = paramString6.substring(j + 1, k);

        String str2 = "cid:img" + localArrayList.size() + "@www.weaver.com.cn";
        paramString6 = Util.StringReplace(paramString6, (String)localObject, str2);

        i = paramString6.indexOf(IMAGE_FLAG, j + str2.length());
        localArrayList.add(localObject);
      }

      processBodyImg(paramString6);

      MimeMultipart localMimeMultipart = new MimeMultipart();

      MimeBodyPart localMimeBodyPart = new MimeBodyPart();

      localMimeBodyPart.setContent(paramString6, "text/html;  charset=\"" + str1 + "\"");
      localMimeBodyPart.addHeader("Content-Transfer-Encoding", "base64");
      Object localObject = new BASE64Encoder();
      paramString6 = ((BASE64Encoder)localObject).encode(paramString6.getBytes());

      localMimeMultipart.setSubType("mixed");
      localMimeMultipart.addBodyPart(localMimeBodyPart);

      for (int m = 0; m < localArrayList.size(); m++)
      {
        localMimeBodyPart = new MimeBodyPart();
        localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource((String)localArrayList.get(m))));

        localMimeBodyPart.setHeader("Content-ID", "<img" + m + "@www.weaver.com.cn>");

        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      m = paramArrayList1.size();
      InputStream localInputStream;
      for (int n = 0; n < m; n++) {
        localMimeBodyPart = new MimeBodyPart();
        localInputStream = (InputStream)paramArrayList2.get(n);
        String str3 = new String(((String)paramArrayList1.get(n)).getBytes("UTF-8"), "ISO8859_1");
        String str4 = FileTypeMap.getDefaultFileTypeMap().getContentType(str3.toLowerCase());

        localMimeBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(localInputStream, str4)));
        localMimeBodyPart.setFileName(str3);
        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.saveChanges();

      Transport.send(localMimeMessage);

      for (n = 0; n < m; n++) {
        localInputStream = (InputStream)paramArrayList2.get(n);
        try {
          localInputStream.close();
        } catch (Exception localException2) {
          writeLog(localException2);
        }
      }

      return true;
    } catch (Exception localException1) {
      writeLog(localException1);
    }return false;
  }

  public boolean sendMiltipartHtml(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt1, ArrayList paramArrayList1, ArrayList paramArrayList2, String paramString7, Hashtable paramHashtable, int paramInt2, Map paramMap)
  {
    Session localSession = null;
    RecordSet localRecordSet = new RecordSet();

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);

    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2, true));
      if (paramString3 != null)
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      if (paramString4 != null)
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      String str1 = null;
      switch (paramInt1)
      {
      case 1:
        str1 = "iso-8859-1";
        break;
      case 2:
        str1 = "big5";
        break;
      case 3:
        str1 = "UTF-8";
      }

      processBodyImg(paramString6);

      int i = paramString6.indexOf(IMAGE_FLAG);
      ArrayList localArrayList1 = new ArrayList();
      String str2 = "0";
      int j = 0;
      int k;
      while (i != -1) {
        j = i;
        i = paramString6.indexOf("src=\"", i + 20);

        str2 = paramString6.substring(j + 19, i - 1);
        str2 = str2.substring(1, str2.indexOf(34));
        k = paramString6.indexOf("\"", i);
        int m = paramString6.indexOf("\"", k + 1);
        localObject1 = paramString6.substring(k + 1, m);
        localObject2 = "cid:img" + str2 + "@www.weaver.com.cn";

        if ((((String)localObject1).indexOf("weaver.email.FileDownloadLocation") != -1) || (((String)localObject1).indexOf("weaver.file.FileDownload") != -1)) {
          i = paramString6.indexOf(IMAGE_FLAG, k + ((String)localObject1).length());
          continue;
        }
        paramString6 = Util.StringReplace(paramString6, (String)localObject1, (String)localObject2);
        i = paramString6.indexOf(IMAGE_FLAG, k + ((String)localObject2).length());
        localArrayList1.add(str2);
      }

      processBodyImg(paramString6);

      if (paramInt2 > 0) {
        k = Util.getSeparator();
        localRecordSet.executeProc("MailResourceContentUpdate", "" + paramInt2 + k + paramString6);
      }

      ArrayList localArrayList2 = new ArrayList();

      Object localObject2 = new Perl5Compiler();
      Object localObject1 = new Perl5Matcher();

      Pattern localPattern = ((PatternCompiler)localObject2).compile("<img.*?src=['\"\\s]?(/.*?weaver.email.FileDownloadLocation\\?fileid=(\\d*)).*?>", 1);

      PatternMatcherInput localPatternMatcherInput = new PatternMatcherInput(paramString6);
      int n = 0;
      String str3 = "";
      String str4 = "";
      MatchResult localMatchResult;
      while (((PatternMatcher)localObject1).contains(localPatternMatcherInput, localPattern)) {
        localMatchResult = ((PatternMatcher)localObject1).getMatch();
        paramString6 = Util.StringReplace(paramString6, localMatchResult.group(1), "cid:img" + n + "@www.weaver.com.cn");
        localArrayList2.add(localMatchResult.group(2));
        n++;
      }

      ArrayList localArrayList3 = new ArrayList();
      localPatternMatcherInput = new PatternMatcherInput(paramString6);
      localPattern = ((PatternCompiler)localObject2).compile("<img.*?src=['\"\\s]?(/.*?weaver.file.FileDownload\\?fileid=(\\d*)).*?>", 1);
      while (((PatternMatcher)localObject1).contains(localPatternMatcherInput, localPattern)) {
        localMatchResult = ((PatternMatcher)localObject1).getMatch();
        paramString6 = Util.StringReplace(paramString6, localMatchResult.group(1), "cid:img" + n + "@www.weaver.com.cn");
        localArrayList3.add(localMatchResult.group(2));
        n++;
      }

      MimeMultipart localMimeMultipart = new MimeMultipart();

      MimeBodyPart localMimeBodyPart = new MimeBodyPart();

      localMimeBodyPart.setContent(paramString6, "text/html;  charset=\"" + str1 + "\"");
      localMimeBodyPart.addHeader("Content-Transfer-Encoding", "base64");
      BASE64Encoder localBASE64Encoder = new BASE64Encoder();
      paramString6 = localBASE64Encoder.encode(paramString6.getBytes());

      localMimeMultipart.setSubType("mixed");
      localMimeMultipart.addBodyPart(localMimeBodyPart);
      Object localObject3;
      String str6;
      Object localObject4;
      for (int i1 = 0; i1 < localArrayList1.size(); i1++) {
        try {
          String str5 = (String)paramHashtable.get(localArrayList1.get(i1).toString());
          localObject3 = str5.substring(str5.lastIndexOf("\\") + 1);

          str6 = (String)localArrayList1.get(i1);

          localMimeBodyPart = new MimeBodyPart();
          localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str6)));

          localMimeBodyPart.setHeader("Content-Type", "image/gif");
          localMimeBodyPart.setHeader("Content-ID", "<img" + localArrayList1.get(i1) + "@www.weaver.com.cn>");

          localMimeMultipart.addBodyPart(localMimeBodyPart);

          localObject4 = "INSERT INTO MailResourceFile (mailid,filename,attachfile,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize) VALUES (" + paramInt2 + ",'" + (String)localObject3 + "',null,'image/gif','" + str5 + "','0','0','0','img" + localArrayList1.get(i1) + "@www.weaver.com.cn','0',0)";
          localRecordSet.executeSql((String)localObject4);
        }
        catch (NullPointerException localNullPointerException) {
          writeLog(localNullPointerException);
        }
      }

      i1 = paramArrayList1.size();

      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext()) {
        localMimeBodyPart = new MimeBodyPart();
        localObject3 = (Map.Entry)localIterator.next();
        str6 = (String)((Map.Entry)localObject3).getKey();
        localObject4 = (DataHandler)((Map.Entry)localObject3).getValue();
        localMimeBodyPart.setDataHandler((DataHandler)localObject4);
        localMimeBodyPart.setFileName(str6);

        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      for (int i2 = 0; i2 < localArrayList2.size(); i2++) {
        str3 = (String)localArrayList2.get(i2);
        localRecordSet.executeSql("SELECT isaesencrypt,aescode,filerealpath FROM MailResourceFile WHERE id=" + str3 + "");
        localRecordSet.next();
        str4 = localRecordSet.getString("filerealpath");
        str6 = localRecordSet.getString("isaesencrypt");
        localObject4 = localRecordSet.getString("aescode");

        localMimeBodyPart = new MimeBodyPart();
        localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str4, str6, (String)localObject4)));
        localMimeBodyPart.setHeader("Content-Type", "image/gif");
        localMimeBodyPart.setHeader("Content-ID", "<img" + i2 + "@www.weaver.com.cn>");
        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      for (i2 = 0; i2 < localArrayList3.size(); i2++) {
        str3 = (String)localArrayList3.get(i2);

        localMimeBodyPart = new MimeBodyPart();
        localMimeBodyPart.setDataHandler(new DataHandler(new FileDataSource(str3)));
        localMimeBodyPart.setHeader("Content-Type", "image/gif");
        localMimeBodyPart.setHeader("Content-ID", "<img" + i2 + "@www.weaver.com.cn>");
        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.saveChanges();

      Transport.send(localMimeMessage);

      return true;
    } catch (Exception localException) {
      writeLog(localException);
    }return false;
  }

  public boolean sendMiltipartText(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, ArrayList paramArrayList1, ArrayList paramArrayList2, String paramString7, Map paramMap)
  {
    Session localSession = null;

    Properties localProperties = new Properties();
    localProperties.put("mail.smtp.from", paramString1);
    localProperties.put("mail.from", paramString1);

    localSession = setSSLConnectMsg(localSession, localProperties);

    MimeMessage localMimeMessage = new MimeMessage(localSession);
    try {
      localMimeMessage.setFrom(new InternetAddress(paramString1, this.accountName));
      localMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(paramString2, true));
      if (paramString3 != null)
        localMimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(paramString3, true));
      if (paramString4 != null) {
        localMimeMessage.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(paramString4, true));
      }
      int i = paramArrayList1.size();

      MimeBodyPart[] arrayOfMimeBodyPart = new MimeBodyPart[i + 1];
      for (int j = 0; j < i + 1; j++) {
        arrayOfMimeBodyPart[j] = new MimeBodyPart();
      }

      MimeMultipart localMimeMultipart = new MimeMultipart();

      arrayOfMimeBodyPart[0].setText(paramString6);
      localMimeMultipart.addBodyPart(arrayOfMimeBodyPart[0]);

      Iterator localIterator = paramMap.entrySet().iterator();
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      String str;
      DataHandler localDataHandler;
      while (localIterator.hasNext()) {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        str = (String)localEntry.getKey();
        localDataHandler = (DataHandler)localEntry.getValue();
        localArrayList1.add(str);
        localArrayList2.add(localDataHandler);
      }
      for (int k = 0; k < localArrayList1.size(); k++) {
        str = (String)localArrayList1.get(k);
        localDataHandler = (DataHandler)localArrayList2.get(k);
        arrayOfMimeBodyPart[(k + 1)].setDataHandler(localDataHandler);
        arrayOfMimeBodyPart[(k + 1)].setFileName(str);
        localMimeMultipart.addBodyPart(arrayOfMimeBodyPart[(k + 1)]);
      }

      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.setSubject(paramString5);
      localMimeMessage.setSentDate(new Date());
      localMimeMessage.setHeader("X-Mailer", "weaver");
      if ((null != this.needReceipt) && ("1".equals(this.needReceipt))) {
        localMimeMessage.setHeader("Disposition-Notification-To", paramString1);
      }
      if (paramString7 != null) localMimeMessage.setHeader("X-Priority", paramString7);

      Transport.send(localMimeMessage);

      return true;
    } catch (Exception localException) {
      writeLog(localException);
    }return false;
  }

  public boolean saveDraft(String paramString, Hashtable paramHashtable, int paramInt)
  {
    RecordSet localRecordSet = new RecordSet();
    try {
      processBodyImg(paramString);
      int i = paramString.indexOf(IMAGE_FLAG);
      ArrayList localArrayList = new ArrayList();
      String str1 = "0";
      int j = 0;
      String str3;
      String str4;
      while (i != -1) {
        j = i;
        i = paramString.indexOf("src=\"", i + 20);

        str1 = paramString.substring(j + 19, i - 1);
        str1 = str1.substring(1, str1.indexOf(34));

        k = paramString.indexOf("\"", i);
        int m = paramString.indexOf("\"", k + 1);
        str3 = paramString.substring(k + 1, m);
        str4 = "cid:img" + str1 + "@www.weaver.com.cn";

        if (str3.indexOf("weaver.email.FileDownloadLocation") != -1) {
          i = paramString.indexOf(IMAGE_FLAG, k + str3.length());
          continue;
        }
        paramString = Util.StringReplace(paramString, str3, str4);
        i = paramString.indexOf(IMAGE_FLAG, k + str4.length());
        localArrayList.add(str1);
      }

      processBodyImg(paramString);

      for (int k = 0; k < localArrayList.size(); k++) {
        try {
          String str2 = (String)paramHashtable.get(localArrayList.get(k).toString());
          str3 = str2.substring(str2.lastIndexOf("\\") + 1);

          str4 = "INSERT INTO MailResourceFile (mailid,filename,attachfile,filetype,filerealpath,iszip,isencrypt,isfileattrachment,fileContentId,isEncoded,filesize) VALUES (" + paramInt + ",'" + str3 + "',null,'image/gif','" + str2 + "','0','0','0','img" + localArrayList.get(k) + "@www.weaver.com.cn','0',0)";
          localRecordSet.executeSql(str4);
        }
        catch (NullPointerException localNullPointerException) {
          writeLog(localNullPointerException);
        }

      }

      if (paramInt > 0) {
        localRecordSet.executeSql("UPDATE MailResource SET hasHtmlImage='1',content='" + paramString + "' WHERE id=" + paramInt + "");
        if (localRecordSet.getDBType().equals("oracle")) {
          localRecordSet.executeSql("UPDATE MailContent SET mailcontent='" + paramString + "' WHERE mailid=" + paramInt + "");
        }
      }
      return true;
    } catch (Exception localException) {
      System.out.println("SendHtml:" + localException);
      writeLog(localException);
    }return false;
  }

  private List processBodyImg(String paramString)
  {
    if (this.isDebug) {
      System.out.println("Body===============================");
      System.out.println(paramString);
    }
    return null;
  }
}
