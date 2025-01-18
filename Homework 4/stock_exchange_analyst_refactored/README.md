# Refactoring
- - -
## 1. Readability

- Method name changes and code comments to clear up vagueness (especially in repository layer)
- Added application property values for python scripts
- Lowered number of constructor arguments by adding Hash Tables instead of lengthy constructors

## 2. Code Simplicity

### Data Fetching
- Changed the way in which the data fetcher logs the python processes and their progress, using a built-in logger 
  singleton from spring
- Added parallel logging from both Error and Input streams of python processes
- Extraction of the script executing method in the same class, allowing for modular scripts adding and deletion
### Repository Layer
- Extraction of the reading CSV method as a generalized method in a utility class (Singleton)

### Models
- Extraction of agreed-upon (SRS) date & time formats, as well as number formats in a utility class (Singleton)

## 3. Design Patterns
- Added the Template design pattern (`Indicator`), where both `MovingAverage` and `Oscillator` now inherit from
  and additionally implement virtual methods
- Added the Factory design pattern so each `Indicator` could be more easily instantiated
- Added the Singleton design pattern so that the utility classes (mention in 2.) do not get instantiated for no reason
  and are readily accessible statically
- Added the Strategy design pattern to how each of the Indicators would make an indication on whether to Buy or Sell
  (realised through the Function/BiFunction functional interfaces)