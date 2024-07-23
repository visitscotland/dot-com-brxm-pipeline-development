# Release Commands Documentation

This document outlines the commands and steps required to execute a release for the project.

## Starting the Release

To start the release process, follow these steps:

1. **Execute the Command**: Navigate to the root of the project and run the following command:

   ``` shell 
   ci/start-release.sh
   ```

Note: You will need Git Bash or another Unix-compatible terminal to run this command.

2. **Alternative for CMD Users**: If you are using the Windows Command Prompt (CMD), you can start the release with:
   
   ``` shell 
   ci/start-release.bat
   ```

## Completing the Release

Once the release is signed off you, complete it by executing:

   ``` shell 
    ci/finish-release.sh
   ``` 

## Notifications

- **Successful Release**: You will receive an email notification once the artefacts are ready.
- **Failed Release**: If something goes wrong and the artefacts cannot be generated, you will receive an email notification detailing the issue.

### Email Instructions

- Copy the information from the notification email into the release ticket for reference and tracking.

## Monitoring Progress

You can monitor the progress of the release and check log messages for the build at:
[jenkinssb.visitscotland.com](https://jenkinssb.visitscotland.com/job/release-brc.visitscotland.com/)

## Dealing with hotfixes

If a bug is found during regression testing:

1. Drop the artefact from [Nexus](https://repo.visitscotland.com/nexus2/) 
2. The release manager should merge the release branch into the `develop` branch.

The Jenkins job will notice the change to the branch and it will create an artefact automatically

---

For more information about releases please refer to [Confluence](https://confluence.visitscotland.com/display/VCE/.COM+-+Release+Process) 
