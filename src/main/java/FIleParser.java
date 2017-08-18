import com.sun.org.apache.xerces.internal.dom.DeferredAttrImpl;
import javafx.fxml.FXML;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class FIleParser
{
    private static final int TABLE_DOES_NOT_EXIST = 1146;

    private static String element = null;
    private String fileName;
    private static Statement statement;

    public static FIleParser newInstance(File file, String elementToParse)
    {
        element = elementToParse;
        if (file == null)
            return null;
        try
        {
            return new FIleParser(file);
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
            if (e.getErrorCode() == TABLE_DOES_NOT_EXIST) createTable(file);
            else Error.newError(e.getMessage());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static void createTable(File file)
    {
        //TODO what now?
        System.out.println();
    }

    public FIleParser(File file) throws SAXException, ParserConfigurationException, SQLException, IOException
    {
        fileName = file.getAbsoluteFile().getName();
        if (fileName.endsWith(".xml"))
        {
            fileName = fileName.replace(".xml", "");
            if (ClickController.checkTableDelete.isEnabled())
            {
                dropTableRecreate(file);
            } else
            {
                parse(file);
            }
        }
    }

    private void parse(File file) throws IOException, SAXException, ParserConfigurationException, SQLException
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(element);
        for (int i = 0; i < nList.getLength(); i++)
        {
            Node item = nList.item(i);
            NamedNodeMap attributes = item.getAttributes();
            StringBuilder column = new StringBuilder();
            StringBuilder val = new StringBuilder();
            for (int j = 0; j < attributes.getLength(); j++)
            {
                Node attr = attributes.item(j);
                String name = ((DeferredAttrImpl) attr).getName();
                String value = ((DeferredAttrImpl) attr).getValue();
                System.out.println("");

                value = value.replace("'", "")
                        .replace("`", "")
                        .replace("\\", "");
                column.append("`").append(name).append("`");
                val.append("'").append(value).append("'");
                // if name equals to last key of map dont append , at the end
                if (!attr.equals(attributes.item(attributes.getLength() - 1)))
                {
                    column.append(", ");
                    val.append(", ");
                }
            }

            String selectTableSQL2 = "INSERT INTO `" + fileName +
                    "` (" + column.toString() + ") VALUES (" +
                    val.toString() + ")";
            getStatement().executeUpdate(selectTableSQL2);
            System.out.println(selectTableSQL2);
        }
    }

    private void dropTableRecreate(File file)
    {

    }

    public static Statement getStatement()
    {
        return statement;
    }

    public static void setStatement(Statement statement)
    {
        FIleParser.statement = statement;
    }
}