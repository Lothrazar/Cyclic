#!/bin/bash

# you can also use 'genIntellijRuns' or 'genVSCodeRuns' see the Gradle Tasks under 'fg_runs'
./gradlew genEclipseRuns

# you may need to hard refresh sometimes, for example changing versions or mappings

# ./gradlew genEclipseRuns --refresh-dependencies
