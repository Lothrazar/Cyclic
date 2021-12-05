#!/bin/bash

# you can also use 'genIntellijRuns' or 'genVSCodeRuns' see the Gradle Tasks under 'fg_runs'
# ./gradlew genEclipseRuns
./gradlew genIntellijRuns
# you may need to hard refresh sometimes, for example changing versions or mappings

# ./gradlew genEclipseRuns --refresh-dependencies
