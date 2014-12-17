package machinelearning;

/*
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public interface IFeatureProbability<T, K> {

    public float featureProbability(T feature, K category);

}
