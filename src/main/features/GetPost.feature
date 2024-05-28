Feature:

	Verify different GET operations using REST-Assured
	
	Scenario: Verify one authos of the post 
	Given  I perform GET operation for "/post"
	Then I should see the author nams as "Karthik KK"