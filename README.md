# OpenMRS Condition Frequency Module

## Overview
This module extends the OpenMRS Core and REST web services to enhance the `doSearch` method in `ConditionResource2_2` to support sorting conditions by frequency (`count=true`), dynamically counting and ordering them without modifying the data model. Additionally, it introduces DAO and service methods (`getAllConditionsByCreator`) to fetch all conditions created by a specific user (creator), ordered by date created. Both features maintain backward compatibility and support pagination for efficient retrieval.

## REST API Endpoints

### Get Conditions Sorted by Frequency
```
GET /openmrs/ws/rest/v1/condition?count=true&v=full
```

Example Response:
```json
{
    "results": [
        {
            "uuid": "abb4e486-e9e0-4007-9415-1534cfa2e650",
            "display": "Fever",
            "condition": {...},
            "patient": {...},
            "links": [...],
            "chiefComplaintCount": 3
        },
        {
            "uuid": "def4e486-e9e0-4007-9415-1534cfa2e650",
            "display": "Hypertension",
            "condition": {...},
            "patient": {...},
            "links": [...],
            "chiefComplaintCount": 2
        }
    ]
}
```

## Technical Details

### Implementation Approach
The module:
1. Extends the existing `ConditionResource2_2` class
2. Adds frequency counting logic to the `doSearch` method
3. Implements custom sorting based on condition frequency
4. Adds frequency information to both default and full representations
5. Uses existing DAO methods to maintain data integrity

### Key Methods
- `doSearch`: Handles the frequency-based sorting
- `getFrequency`: Calculates frequency for individual conditions
- `getChiefComplaintCount`: Generates unique keys for condition matching
- `getRepresentationDescription`: Defines REST response structure

Building from Source
--------------------
You will need to have Java 1.6+ and Maven 2.x+ installed.  Use the command 'mvn package' to 
compile and package the module.  The .omod file will be in the omod/target folder.

Alternatively you can add the snippet provided in the [Creating Modules](https://wiki.openmrs.org/x/cAEr) page to your 
omod/pom.xml and use the mvn command:

    mvn package -P deploy-web -D deploy.path="../../openmrs-1.8.x/webapp/src/main/webapp"

It will allow you to deploy any changes to your web 
resources such as jsp or js files without re-installing the module. The deploy path says 
where OpenMRS is deployed.

## Installation
1. Build the module to produce the .omod file.
2. Use the OpenMRS `Administration > Manage Modules` screen to upload and install the `.omod` file.

If uploads are not allowed from the web (changable via a runtime property), you can drop the omod
into the `~/.OpenMRS/modules` folder.  (Where `~/.OpenMRS` is assumed to be the Application 
Data Directory that the running openmrs is currently using.)  After putting the file in there 
simply restart `OpenMRS/tomcat` and the module will be loaded and started.

## Configuration
No additional configuration is required. The module extends existing functionality without requiring database changes.

## License
Mozilla Public License, v. 2.0

## Contributing
Feel free to submit pull requests or create issues for bugs and feature requests.
