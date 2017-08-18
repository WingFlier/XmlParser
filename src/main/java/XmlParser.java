import org.w3c.dom.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParser
{
    static String[] attributesUsers =
            {
                    "Id", "Reputation", "CreationDate", "DisplayName", "LastAccessDate",
                    "WebsiteUrl", "Location", "AboutMe", "Views", "UpVotes", "DownVotes",
                    "AccountId"
            };

    static String[] attributesBadges =
            {
                    "Id", "UserId", "Name", "Date", "Class", "TagBased"
            };

    static String[] attributesComments =
            {
                    "Id", "PostId", "Score", "Text", "CreationDate", "UserId"
            };

    static String[] attributesHistory =
            {
                    "Id", "PostHistoryTypeId", "PostId", "RevisionGUID", "CreationDate", "UserId", "Text"
            };

    static String[] attributesPostLinks =
            {
                    "Id", "CreationDate", "PostId", "RelatedPostId"
            };

    static String[] attributesPost =
            {
                    "Id", "PostTypeId", "AcceptedAnswerId", "CreationDate", "Score",
                    "ViewCount", "Body", "OwnerUserId", "LastEditorUserId", "LastEditDate",
                    "LastActivityDate", "Title", "Tags", "AnswerCount", "CommentCount", "FavoriteCount", "CommunityOwnedDate"
            };

    static String[] attributesTags =
            {
                    "Id", "TagName", "Count", "ExcerptPostId", "WikiPostId"
            };

    static String[] attributesVotes =
            {
                    "Id", "PostId", "VoteTypeId", "CreationDate"
            };


    public ArrayList<HashMap<String, String>> parseXmlAttributes(String path, String... attributes)
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("row");

            for (int i = 0; i < nList.getLength(); i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    for (String attribute : attributes)
                    {
                        map.put(attribute, eElement.getAttribute(attribute));
                    }
                }
                list.add(map);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println(path + " parsed");
        return list;
    }

    public String[] getAttributes(File path)
    {
        return new String[0];
    }
}