package quickdt.predictiveModels.downsamplingPredictiveModel;

import quickdt.data.Attributes;
import quickdt.predictiveModels.Classifier;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by ian on 4/22/14.
 */
public class DownsamplingClassifier implements Classifier {
    private static final long serialVersionUID = -265699047882740160L;

    public final Classifier wrappedClassifier;
    private final Serializable minorityClassification;
    private final Serializable majorityClassification;
    private final double dropProbability;

    public DownsamplingClassifier(final Classifier wrappedClassifier, final Serializable majorityClassification, final Serializable minorityClassification, final double dropProbability) {
        this.wrappedClassifier = wrappedClassifier;
        this.majorityClassification = majorityClassification;
        this.minorityClassification = minorityClassification;
        this.dropProbability = dropProbability;
    }

    @Override
    public double predict(final Map<String, Serializable> attributes, final Serializable classification) {
        double uncorrectedProbability = wrappedClassifier.getProbability(attributes, minorityClassification);
        double probabilityOfMinorityInstance = Utils.correctProbability(dropProbability, uncorrectedProbability);
        if (classification.equals(minorityClassification)) {
            return probabilityOfMinorityInstance;
        } else {
            return 1 - probabilityOfMinorityInstance;
        }

    }

    /**
     * Unsupported at this time, will throw UnsupportedOperationException
     * @param attributes
     * @return
     */
    @Override
    public Map<Serializable, Double> getProbabilitiesByClassification(final Map<String, Serializable> attributes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dump(final Appendable appendable) {
        try {
            appendable.append("Will predict for downsampling with drop probability "+dropProbability+" for minority classification "+minorityClassification+"\n");
            wrappedClassifier.dump(appendable);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Serializable getClassificationByMaxProb(final Map<String, Serializable> attributes) {
        return wrappedClassifier.getClassificationByMaxProb(attributes);
    }

    public double getDropProbability() {
        return dropProbability;
    }

    public Serializable getMajorityClassification() {
        return majorityClassification;
    }
}