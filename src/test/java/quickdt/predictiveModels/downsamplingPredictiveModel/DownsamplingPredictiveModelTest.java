package quickdt.predictiveModels.downsamplingPredictiveModel;

import junit.framework.Assert;
import org.testng.annotations.Test;
import quickdt.data.Attributes;
import quickdt.data.HashMapAttributes;
import quickdt.predictiveModels.PredictiveModel;

import static org.mockito.Mockito.*;

/**
 * Created by ian on 4/24/14.
 */
public class DownsamplingPredictiveModelTest {
    @Test
    public void simpleTest() {
        final PredictiveModel<Object> wrappedPredictiveModel = mock(PredictiveModel<Object>.class);
        when(wrappedPredictiveModel.getProbability(any(Map<String, Serializable>.class), eq(Boolean.TRUE))).thenReturn(0.5);
        DownsamplingClassifier downsamplingClassifier = new DownsamplingClassifier(wrappedPredictiveModel, Boolean.FALSE, Boolean.TRUE, 0.9);
        double corrected = downsamplingClassifier.getProbability(new HashMapAttributes(), Boolean.TRUE);
        double error = Math.abs(corrected - 0.1/1.1);
        Assert.assertTrue(String.format("Error (%s) should be negligible", error), error < 0.0000001);
    }
}