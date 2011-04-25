package com.beenthere.kml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class KMLParser {
    
    /**
     * Android compatible KML parser. 
     * Creates a KML object that represents the structure of the kml
     * file passed as argument.
     * @author Jordan Bonnet
     * @param file
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */ 
	public static Kml parse(File file) throws XmlPullParserException, IOException {
		final Kml kml = new Kml();
		final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new FileReader(file));
		int eventType = xpp.getEventType();
		
		Stack<Node> mStack = new Stack<Node>();
		String currentTag = null;
		boolean isNode = true;
		
		while (eventType != XmlPullParser.END_DOCUMENT) {
			
			if (eventType == XmlPullParser.START_DOCUMENT) {
				mStack.push(kml);
			} 
			
			else if (eventType == XmlPullParser.END_DOCUMENT) {
				break;
			} 
			
			else if (eventType == XmlPullParser.START_TAG) {
				currentTag = xpp.getName();
				Node currentNode = mStack.peek();
				Node newNode = currentNode.handleTag(currentTag, null);
				if (newNode == null) {
					isNode = false;
					mStack.push(currentNode);
				}
				else {
					isNode = true;
					mStack.push(newNode);
				}
			} 
			
			else if (eventType == XmlPullParser.END_TAG) {
				mStack.pop();
				currentTag = null;
			} 
			
			else if (eventType == XmlPullParser.TEXT) {
				if (currentTag != null && !isNode) {
					String text = xpp.getText();
					Node node = mStack.peek();
					node.handleTag(currentTag, text);
				}
			}
			
			eventType = xpp.next();
		}
		return kml;
	}
}
