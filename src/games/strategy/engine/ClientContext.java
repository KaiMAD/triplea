package games.strategy.engine;

import games.strategy.engine.config.GameEnginePropertyFileReader;
import games.strategy.engine.config.PropertyReader;
import games.strategy.engine.framework.map.download.MapDownloadController;
import games.strategy.engine.framework.map.download.MapDownloadStrategy;
import games.strategy.engine.framework.map.download.MapListingSource;

/**
 * IOC container for storing objects needed by the TripleA Swing client
 * A full blow dependency injection framework would deprecate this class.
 *
 * This class roughly follows the singleton pattern. The singleton instance
 * can be updated, this is allowed to enable a mock instance of this class to
 * be used.
 *
 * Caution: the public API of this class will grow to be fairly large. For every object we wish to return, we'll have an
 * "object()" method that will returns that same object. When things become hard to manage it'll be a good time
 * to move to an annotation or configuration based IOC framework.
 *
 * Second note, try to put as much class specific construction logic into the constructor of each class managed by this
 * container. This class should focus on just creating and wiring classes together. Contrast that with generating the
 * data
 * needed to create classes. For example, instead of parsing a file and passing that value to the constructor of another
 * class,
 * we would instead create an intermediary class that knows everything about which file to parse and how to parse it,
 * and we would
 * pass that intermediary class to the new class we wish to create. Said in another way, this class should not contain
 * any 'business'
 * logic.
 *
 * Third Note: Any classes created by ClientContext cannot call ClientContext in their constructor, all dependencies
 * must be passed to them.
 * Since GameRunner2 creates ClientContext, similar none of the classes created by Client Context can game runner 2
 */
public final class ClientContext {
  private static final ClientContext instance = new ClientContext();

  private final MapDownloadController mapDownloadController;
  private final EngineVersion engineVersion;
  private final MapDownloadStrategy downloadStrategy;
  private final PropertyReader propertyReader;


  private ClientContext() {
    propertyReader = new GameEnginePropertyFileReader();
    MapListingSource listingSource = new MapListingSource(propertyReader);
    mapDownloadController = new MapDownloadController(listingSource);
    engineVersion = new EngineVersion(propertyReader);
    downloadStrategy = new MapDownloadStrategy();
  }

  public static PropertyReader propertyReader() {
    return instance.propertyReader;
  }

  public static MapDownloadController mapDownloadController() {
    return instance.mapDownloadController;
  }

  public static EngineVersion engineVersion() {
    return instance.engineVersion;
  }

  private MapDownloadStrategy downloadStrategy() {
      return downloadStrategy;
  }
  public static MapDownloadStrategy mapDownloadStrategy() {
    return instance.downloadStrategy();
  }
}
