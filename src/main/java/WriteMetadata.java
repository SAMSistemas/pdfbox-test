import java.io.*;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.jempbox.xmp.XMPMetadata;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;


public class WriteMetadata
{

    static public void main(String args[])
    {
        PDDocument document=null;
        InputStream in=null;
        try
        {
            String xmpIn=null;
            String pdfIn=null;
            String pdfOut=null;
            Document xmpDoc=null;
            int optind=0;
            while(optind<args.length)
            {
                if(args[optind].equals("-h"))
                {
                    System.err.println("Pierre Lindenbaum PhD. 2010");
                    System.err.println("-h this screen");
                    System.err.println("-pdfin|-i <pdf-in>");
                    System.err.println("-xmpin|-x <xmp-in>");
                    System.err.println("-pdfout|-o <pdf-out>");
                    return;
                }
                else if(args[optind].equals("-xmpin") || args[optind].equals("-x"))
                {
                    xmpIn= args[++optind];
                }
                else if(args[optind].equals("-pdfin") || args[optind].equals("-i"))
                {
                    pdfIn= args[++optind];
                }
                else if(args[optind].equals("-pdfout") || args[optind].equals("-o"))
                {
                    pdfOut= args[++optind];
                }
                else if (args[optind].equals("--"))
                {
                    ++optind;
                    break;
                }
                else if (args[optind].startsWith("-"))
                {
                    System.err.println("bad argument " + args[optind]);
                    System.exit(-1);
                }
                else
                {
                    break;
                }
                ++optind;
            }
            if(optind!=args.length)
            {
                System.err.println("Illegal number of arguments");
                return;
            }
            if(pdfIn==null)
            {
                System.err.println("pdf-in missing");
                return;
            }
            if(pdfOut==null)
            {
                System.err.println("pdf-out missing");
                return;
            }
            if(pdfIn.equals(pdfOut))
            {
                System.err.println("pdf-out is same as pdf-in");
                return;
            }
            if(xmpIn==null)
            {
                System.err.println("XMP missing");
                return;
            }
            else
            {
                DocumentBuilderFactory f= DocumentBuilderFactory.newInstance();
                f.setExpandEntityReferences(true);
                f.setIgnoringComments(true);
                f.setIgnoringElementContentWhitespace(true);
                f.setValidating(false);
                f.setCoalescing(true);
                f.setNamespaceAware(true);
                DocumentBuilder builder=f.newDocumentBuilder();
                xmpDoc= builder.parse(xmpIn);
            }

            in=new FileInputStream(pdfIn);
            PDFParser parser=new PDFParser(in);
            parser.parse();
            document=  parser.getPDDocument();
            if(document.isEncrypted())
            {
                System.err.println("Warning ! Document is Encrypted!");
            }
            PDDocumentCatalog cat = document.getDocumentCatalog();
            PDMetadata metadata = new PDMetadata(document);
            metadata.importXMPMetadata(new XMPMetadata(xmpDoc));
            cat.setMetadata(metadata);
            document.save(pdfOut);
        }
        catch(Throwable err)
        {
            err.printStackTrace();
        }
        finally
        {
            if(document!=null) try { document.close();} catch(Throwable err2) {}
            if(in!=null) try { in.close();} catch(Throwable err2) {}
        }
    }
}