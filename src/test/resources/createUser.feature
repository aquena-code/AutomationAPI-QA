Feature: Users

  Scenario: I want to create a new user

    Given I have authentication to todo.ly
    When I send POST request 'api/user.json' with json an BASIC authentication

    """
    {
      "Email":"alvaro32@email.com",
      "FullName": "Alvaro Quena",
      "Password": "Alvaro123"
    }
    """
    Then I expected the response code 200

    And I expected the response body is equal
  """
  {
    "Id": EXCLUDE,
    "Email": "alvaro32@email.com",
    "Password": null,
    "FullName": "Alvaro Quena",
    "TimeZone": 0,
    "IsProUser": false,
    "DefaultProjectId": EXCLUDE,
    "AddItemMoreExpanded": false,
    "EditDueDateMoreExpanded": false,
    "ListSortType": 0,
    "FirstDayOfWeek": 0,
    "NewTaskDueDate": -1,
    "TimeZoneId": "Pacific Standard Time"
  }
  """

    And I get the property value 'Id' and save on ID_USER
    And I get the property value 'FullName' and save on UserName

      When I send PUT request 'api/user/ID_USER.json' with json an BASIC authentication
     """
      {
        "FullName" : "AlvaroQ"
      }
    """
    Then I expected the response code 200

    When I send GET request 'api/user.json' with json an BASIC authentication
    """

    """
    Then I expected the response code 200