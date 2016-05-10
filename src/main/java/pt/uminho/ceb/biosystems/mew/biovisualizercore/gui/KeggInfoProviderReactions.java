package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.JCSWrapper;
import pt.uminho.ceb.biosystems.mew.utilities.io.IOUtils;

public class KeggInfoProviderReactions {
	
	public static final String					KEGG_IMAGE_PROVIDER_CACHE_GROUP	= "KEGG_IMAGE_PROVIDER_CACHE";
	public static final String					KEGG_NAME_PROVIDER_CACHE_GROUP	= "KEGG_NAME_PROVIDER_CACHE";
	private static String						DEFAULT_JCS_DIRECTORY			= ".";
	
	public static final String					KEGG_REST						= "http://rest.kegg.jp/";
	private static final HttpClientParams		params							= new HttpClientParams();
	private static JCSWrapper<String, String>	_imageCache						= null;
	private static JCSWrapper<String, String>	_nameCache						= null;
	
	public static JCSWrapper<String, String> getImageCache() {
		if (_imageCache == null) _imageCache = new JCSWrapper<>(KEGG_IMAGE_PROVIDER_CACHE_GROUP, getDEFAULT_JCS_DIRECTORY());
		
		return _imageCache;
	}
	
	public static JCSWrapper<String, String> getNameCache() {
		if (_nameCache == null) _nameCache = new JCSWrapper<>(KEGG_NAME_PROVIDER_CACHE_GROUP, getDEFAULT_JCS_DIRECTORY());
		
		return _nameCache;
	}
	
	public static String getDEFAULT_JCS_DIRECTORY() {
		return DEFAULT_JCS_DIRECTORY;
	}
	
	public static void setDEFAULT_JCS_DIRECTORY(String dEFAULT_JCS_DIRECTORY) {
		DEFAULT_JCS_DIRECTORY = dEFAULT_JCS_DIRECTORY;
	}
	
	public static BufferedImage getImageForKEGGID(String keggID) throws IOException {
		String byteArrayString = getImageCache().get(keggID);
		byte[] stream = null;
		BufferedImage image = null;
		if (byteArrayString != null && !byteArrayString.isEmpty()) {
			stream = recoverBytesFromString(byteArrayString);
		} else {
			String url = KEGG_REST + "get/" + keggID + "/image";
			stream = getImageFromURL(url);
			if (stream != null) {
				getImageCache().put(keggID, Arrays.toString(stream));
			}
		}
		if (stream != null) image = ImageIO.read(new ByteArrayInputStream(stream));
		
		return image;
	}
	
	public static byte[] recoverBytesFromString(String byteString) {
		String[] byteValues = byteString.substring(1, byteString.length() - 1).split(",");
		byte[] bytes = new byte[byteValues.length];
		
		for (int i = 0, len = bytes.length; i < len; i++) {
			bytes[i] = Byte.valueOf(byteValues[i].trim());
		}
		return bytes;
	}
	
	public static String[] getNamesForKEGGID(String keggID) throws IOException {
		String[] names = null;
		String nameString = getNameCache().get(keggID);
		if (nameString == null || nameString.isEmpty()) {
			String url = KEGG_REST + "find/reaction/" + keggID;
			String streamBody = getNamesFromURL(url);
			if (streamBody != null && !streamBody.isEmpty()) {
				nameString = streamBody.split("\t")[1];
				getNameCache().put(keggID, nameString);
			}
		}
		
		if (nameString != null && !nameString.isEmpty()) {			
			names = nameString.split(";");
			for(int i=0; i<names.length; i++)
				names[i] = names[i].trim();
		}
		
		return names;
	}
	
	/**
	 * @param operation
	 * @param args
	 * @return
	 */
	private static byte[] getImageFromURL(String url) {
		
		byte[] body = null;
		
		HttpClient client = new HttpClient();
		client.setParams(params);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		if (LayoutVisualizerGUI.getProxy() != null) client.getParams().setParameter("http.route.default-proxy", LayoutVisualizerGUI.getProxy());
		
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		try {
			
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				
				System.err.println("Method failed: " + method.getStatusLine());
				System.err.println("get URL: " + url);
			} else {
				//				body = method.getResponseBody();
				InputStream stream = method.getResponseBodyAsStream();
				body = IOUtils.toByteArray(stream);
			}
			
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		
		return body;
	}
	
	private static String getNamesFromURL(String url) {
		String body = null;
		
		HttpClient client = new HttpClient();
		client.setParams(params);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		if (LayoutVisualizerGUI.getProxy() != null) client.getParams().setParameter("http.route.default-proxy", LayoutVisualizerGUI.getProxy());
		
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		try {
			
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				
				System.err.println("Method failed: " + method.getStatusLine());
				System.err.println("get URL: " + url);
			} else
				body = method.getResponseBodyAsString();
			
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		
		return body;
	}
	
	public static JComponent buildImageComponent(List<BufferedImage> images) {
		JPanel imagePanel = new JPanel(true);
		imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(imagePanel);
		
		for (BufferedImage image : images) {
			JLabel lab = new JLabel();
			lab.setIcon(new ImageIcon(image));
			imagePanel.add(lab);
		}
		
		return scroll;
	}
}
