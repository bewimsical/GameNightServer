package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.model.SearchGame;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import org.springframework.web.client.RestTemplate;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APIClient {
    private Document doc;
    private XPath xpath;

    public APIClient(String endpoint, String query){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://boardgamegeek.com/xmlapi/"+endpoint+"/"+query;

        //Call API and get response as XML string
        String xmlResponse = restTemplate.getForObject(url, String.class);

        try {
            // Create XPath instance
            XPathFactory xPathFactory = XPathFactory.newInstance();
            this.xpath = xPathFactory.newXPath();

            // Convert XML string to a Document object
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.doc = builder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getField(String selector){
        return getField(selector, doc);
    }

    public String getField(String selector, Object itemNode){
        if (itemNode == null) return null;
        try {
            XPathExpression expr = xpath.compile(selector);
            Node node = (Node) expr.evaluate(itemNode, XPathConstants.NODE);
            // Extract values
            return (node != null) ? node.getTextContent() : "Not Found";
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SearchGame> getSearchResults(){
        try {
            // Query for all <name> elements
            XPathExpression expr = xpath.compile("//boardgame");
            NodeList gameNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            // Convert NodeList to ArrayList<String>
            List<SearchGame> gameList = new ArrayList<>();
            for (int i = 0; i < gameNodes.getLength(); i++) {
                Node gameNode = gameNodes.item(i);
                int objectId = Integer.parseInt(gameNode.getAttributes().getNamedItem("objectid").getNodeValue());
                String name = getField("./name[@primary='true']", gameNode);
                String year = getField("./yearpublished", gameNode);
                gameList.add(new SearchGame(objectId, name,year));
            }
            return gameList;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }



//    public static void main(String[] args) {
//
//
//
//
//            // Query for <name> element where @primary="true"
//            XPathExpression expr = xpath.compile("//name[@primary='true']");
//            Node primaryNameNode = (Node) expr.evaluate(doc, XPathConstants.NODE);
//            // Extract values
//            String primaryName = (primaryNameNode != null) ? primaryNameNode.getTextContent() : "Not Found";
//
//
//
//            // Query for <age> element
//            expr = xpath.compile("//age");
//            Node ageNode = (Node) expr.evaluate(doc, XPathConstants.NODE);
//            // Extract values
//            String age = (ageNode != null) ? ageNode.getTextContent() : "Not Found";
//
//
//
//            // Print results
//            System.out.println("Primary Name: " + primaryName);
//            System.out.println("Age: " + age);
//
//
//    }
}
