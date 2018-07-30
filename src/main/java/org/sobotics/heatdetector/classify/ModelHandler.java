package org.sobotics.heatdetector.classify;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sobotics.heatdetector.rest.classify.HeatClassifier;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class ModelHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(HeatClassifier.class);
	private static ModelHandler instance;
	
	private DocumentCategorizerME openNLPClassifier;
	private Classifier wekaNBClassifier;
	private Instances wekaARFF;

	
	private ModelHandler(){
		try {
			initModels();
		} catch (Exception e) {
			logger.error("initModels",e);
		}
	}

	public static ModelHandler getInstance(){
		if (instance==null){
			instance = new ModelHandler();
		}
		return instance;
	}


	
	private void initModels() throws Exception {


		// Open NLP classifier
		long timer = System.currentTimeMillis();
		
		DoccatModel m = new DoccatModel(new File("model/open_comments.model"));
		openNLPClassifier = new DocumentCategorizerME(m);
		
		if (logger.isDebugEnabled()) {
			logger.debug("initModels() - OpenNLP Time to load: " + (System.currentTimeMillis() - timer));
		}

		timer = System.currentTimeMillis();
		
		// Weka NaiveBayes classifier
		wekaNBClassifier = (Classifier) SerializationHelper.read(new FileInputStream("model/nb_comments.model"));

		if (logger.isDebugEnabled()) {
			logger.debug("initModels() - Weka Time to load: " + (System.currentTimeMillis() - timer));
		}
		
		// This needs to be removed, only used to copy the structure when classifing
		wekaARFF = getInstancesFromARFF("model/comments.arff");
		wekaARFF.setClassIndex(wekaARFF.numAttributes() - 1);

	}
	
	/**
	 * Classify with weka
	 * @param classifyText, needs to be pre processed
	 * @return array length 2 [0]=good, [1]=bad
	 * @throws Exception
	 */
	public double[] classifyMessageNaiveBayes(String classifyText) throws Exception {
		Instances ins = createArff(classifyText);
		return wekaNBClassifier.distributionForInstance(ins.get(0));
	}
	
	/**
	 * Classify with open nlp
	 * @param classifyText, needs to be pre processed
	 * @return array length 2 [0]=good, [1]=bad
	 * @throws Exception
	 */
	public double[] classifyMessageOpenNLP(String classifyText) {
		return openNLPClassifier.categorize(new String[]{classifyText});
	}
	
	
	/**
	 * Get Instances from ARFF file
	 * 
	 * @param fileLocation
	 *            path to ARFF file
	 * @return Instances of given ARFF file
	 */
	private Instances getInstancesFromARFF(String fileLocation) {
		Instances instances = null;
		try {
			DataSource dataSource = new DataSource(fileLocation);
			instances = dataSource.getDataSet();
		} catch (Exception ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("getInstancesFromARFF(String) - Can't find ARFF file at given location: " + fileLocation);
			}
		}

		return instances;
	}

	
	private Instances createArff(String content) {

		ArrayList<Attribute> atts = new ArrayList<>();
		ArrayList<String> classes = new ArrayList<>();
		classes.add("good");
		classes.add("bad");

		atts.add(new Attribute("text", (ArrayList<String>) null));

		// make sure that the name of the class attribute is unlikely to
		// clash with any attribute created via the StringToWordVector filter
		atts.add(new Attribute("@@class@@", classes));
		Instances data = new Instances("weka_SO_comments_model", atts, 0);
		data.setClassIndex(data.numAttributes() - 1);

		DenseInstance di = new DenseInstance(2);
		Attribute messageAtt = data.attribute("text");
		di.setValue(messageAtt, messageAtt.addStringValue(content));

		data.add(di);

		return data;
	}


}
