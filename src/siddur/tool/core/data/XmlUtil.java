package siddur.tool.core.data;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {
	private static final String PLUGIN = "plugin";
	private static final String INPUTMODEL = "inputModel";
	private static final String OUTPUTMODEL = "outputModel";
	
	private static final String DATA = "data";
	private static final String TYPE = "type";
	private static final String TAG = "tag";
	private static final String DESCRIPTION = "description";
	private static final String CONSTRAINT = "constraint";
	
	

	public static void toXml(ToolDescriptor pd, File file) throws Exception{
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement(PLUGIN);
		
		if(pd != null){
			root.addElement("ID").addText(pd.getPluginID());
			root.addElement("lang").addText(pd.getLang());
			root.addElement("keywords").addText(pd.getKeywords());
			root.addElement("name").addText(pd.getPluginName());
			root.addElement("authorId").addText(pd.getAuthorId());
			root.addElement("catalog").addText(pd.getCatalog());
			root.addElement("icon").addText(pd.getIcon());
			root.addElement("description").addText(pd.getDescription());
		}
		
		DataTemplate[] input = pd.getInputModel();
		if(input != null && input.length > 0){
			Element inputParent = root.addElement(INPUTMODEL);
			tdToXml(inputParent, input);
		}
		
		DataTemplate[] output = pd.getOutputModel();
		if(output != null && output.length > 0){
			Element inputParent = root.addElement(OUTPUTMODEL);
			tdToXml(inputParent, output);
		}
		
		OutputFormat o = new OutputFormat("  ", true);
		XMLWriter w = new XMLWriter(new FileOutputStream(file), o);
		w.write(doc);
	}
	
	private static void tdToXml(Element parent, DataTemplate[] tds){
		for(DataTemplate td : tds){
			Element item = parent.addElement(DATA);
			item.addElement(TYPE).addText(td.getDataType());
			item.addElement(TAG).addText(td.getTag());
			item.addElement(DESCRIPTION).addText(td.getDescription());
			item.addElement(CONSTRAINT).addText(td.getConstraint());
		}
	}
	
	public static ToolDescriptor fromXml(File file) throws Exception{
		if(!file.isFile()) return null;
		Document doc = new SAXReader().read(file);
		Element root = doc.getRootElement();
		
		ToolDescriptor pd = new ToolDescriptor();
		pd.setPublishAt(new Date(Long.parseLong(root.elementText("ID"))));
		pd.setPluginName(root.elementText("name"));
		pd.setLang(root.elementText("lang"));
		pd.setKeywords(root.elementText("keywords"));
		pd.setCatalog(root.elementText("catalog"));
		pd.setAuthorId(root.elementText("authorId"));
		pd.setIcon(root.elementText("icon"));
		pd.setDescription(root.elementText("description"));
		
		Element inputModel = root.element(INPUTMODEL);
		if(inputModel != null){
			pd.setInputModel(xmlToTd(inputModel));
		}
		
		Element outputModel = root.element(OUTPUTMODEL);
		if(outputModel != null){
			pd.setOutputModel(xmlToTd(outputModel));
		}
		return pd;
	}
	
	private static DataTemplate[] xmlToTd(Element parent){
		@SuppressWarnings("rawtypes")
		List list = parent.elements(DATA);
		DataTemplate[] tds = new DataTemplate[list.size()];
		int x = 0;
		for(Object obj : list){
			Element ele = (Element) obj;
			DataTemplate td = new DataTemplate();
			td.setDataType(ele.elementText(TYPE));
			td.setTag(ele.elementText(TAG));
			td.setDescription(ele.elementText(DESCRIPTION));
			td.setConstraint(ele.elementText(CONSTRAINT));
			tds[x++] = td;
		}
		return tds;
	}
	
}
