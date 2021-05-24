Feature: Projects
    As a user I want to be able to Create my projects

    Scenario Outline: Create an user
        Given I have authentication to todo.ly
        And I send <Method User> request 'api/user.json' with json and BASIC authentication
        """
        {
          "Email":"alvaro34@email.com",
          "FullName": "Alvaro Quena",
          "Password": "Alvaro123"
        }
        """
        When I send <Method Project> request to 'api/projects.json' with the content <Content Project> and BASIC authentication
        Then I expected the response code 200


        Examples:
            |Method User |Method Project|Content Project    |
            |POST        |POST          |Project P-1        |
            |GET         |POST          |Project P-2        |
            |GET         |POST          |Project P-3        |
