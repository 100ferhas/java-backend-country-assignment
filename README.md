# Backend Country Assignment
This library manipulate data from an external [REST API](https://restcountries.com/) that provides information about countries. 
This data get processed, filtered, sorted or anything you wish to implement.

To know how to modify or extend this library please read [this section](#project-design)

## Usage
This library comes in a packaged jar file that can be executed launching it from a command line.

1. Make sure you have [Java](https://www.java.com/) installed on your computer (requires version 11+)
2. Download the [latest release](https://github.com/100ferhas/java-backend-country-assignment/releases/download/latest/backend-country-assignment.jar) 
of this library
3. Open a terminal window from the folder where you downloaded the jar file
4. Start the application using the following command 

```console
foo@bar:~$ java -jar random-user-generator.jar
```

The program wil start, and you'll see by default the output results for all the existing `Processors` in your console.

To know more about Processors refer to the [dedicated section](#processors).

You can also specify where do you want the data forwarded using the only program argument available at this time.

For example:

```console
foo@bar:~$ java -jar random-user-generator.jar file
```

This will write the output to a file in your current working directory.

For available `Forwarder` types refer to [the dedicated section](#forwarders)

## Project Design
The design of the project is pretty straightforward, it has an `AssignmentExecutor` class that retrieve the country data 
from the REST endpoint, and it "feed" all available `Processors` that will process this data, storing the result in an 
internal `List` that will be passed to a `Forwarder`, which is responsible to forward this processed data somewhere.


### Processors
The program uses `Processor` to define different implementation of data extraction.

Following there are the available `Processors` at this time:

| Processor Class              | Description                                                                     |
|------------------------------|---------------------------------------------------------------------------------|
| `DensityPopulationProcessor` | Sorted list of countries by population density in descending order              |
| `AsianBorderProcessor`       | Countries in Asia containing the most bordering countries of a different region |

You can add your custom `Processor` creating your class that extends `AbstractProcessor`, this will be picked at 
application startup.

For example:
```java
public class EuropeanCountryProcessor extends AbstractProcessor {
    @Override
    public String getDescription() {
        // define your process description
        return "My Processor Description";
    }

    @Override
    protected List<RestCountryModel> doProcess(List<RestCountryModel> countries) {
        // define your custom logic
        return countries.stream()
                .filter(restCountryModel -> restCountryModel.getRegion().equals("Europe"))
                .collect(Collectors.toList());
    }

    @Override
    public String getNormalizedData() {
        // convert your data to string
        return Arrays.toString(data.toArray());
    }
}
```

### Forwarders
The program uses `Forwarders` to define different implementation of data extraction.

Following there are the available `Forwarders` at this time:

| Forwarder Class    | Description                                                                                                  | Forwards to | Default |
|--------------------|--------------------------------------------------------------------------------------------------------------|-------------|---------|
| `ConsoleForwarder` | print extracted data to console                                                                              | Console     | Yes     |
| `FileForwarder`    | write extracted data to a file, it will create a file `processed_data.txt` in your current working directory | File        | No      |
| `KafkaForwarder`   | send extracted data to Kafka **(TO BE IMPLEMENTED)**                                                         | Kafka       | No      |
| `RestApiForwarder` | print extracted data to a REST endpoint **(TO BE IMPLEMENTED)**                                              | REST        | No      |

You can create your custom `Forwarder` creating a class that implements `Forwarder` interface. 

For example:
```java
public class DatabaseForwarder implements Forwarder {
    @Override
    public void forward(Processor processor) {
        List<RestCountryModel> data = processor.getData();
        
        // implement your custom logic here
    }
}
```

**NOTE: You must create a custom `ForwarderType` that will be used by users to specify the `Forwarder` to use.**

**You must then register your custom created `Forwarder` in `ForwarderProvider` class, see existing entries**

### Project Dependencies
Following there are the project dependencies:

#### Logging
The project uses `Slf4j` to manage logging. You'll find a log file showing logs related to the latest execution of the application.
Just look for `debug.log` file in your current working directory.

`Slf4j` is also used by `FileForwarder` to write output to files.

#### OpenFeign + Jackson
[OpenFeign](https://github.com/OpenFeign/feign) library is used to execute HTTP request. 
`Jackson` is used for models serialization.

Within the project has been also implemented a retry mechanism to repeat non-200 HTTP responses to the Country REST APIs.

#### JUnit + Mockito
Unit test were written using `JUnit` and `Mockito` to mock data while testing.

#### Project Lombok
Project Lombok is used within the project using its annotation to simplify code.

#### Reflections
Reflections library is used to initialize application at startup.