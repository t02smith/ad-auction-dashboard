# Campaigns

A campaign is where we will store information about a specific campaign. This includes:

* The name of the campaign
* The file locations of the impressions, click and server logs
* The read and parsed versions of these files when appropriate
* Whether the data is currently loaded

By collecting files as part of a campaign it makes it easier to tell which files relate to which and we can perform calculations with access to the entire dataset. This is useful as some calculations will require more than one data file.

## CampaignManager

The CampaignManager class is responsible for:

* Generating new campaign classes
* Initiating a read for campaign data files
* Saving campaign metadata to a file
* Reading metadata from a file

The Model class will interact with the CampaignManager class to orchestrate the above functionality. It also must be able to store metadata about campaigns to a file so that a user's campaign list will persist after the program shuts down.