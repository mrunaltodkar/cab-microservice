package com.capgemini.cab.driversignupsigninservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capgemini.cab.driversignupsigninservice.entity.Driver;
import com.capgemini.cab.driversignupsigninservice.entity.DriverDetails;
import com.capgemini.cab.driversignupsigninservice.entity.User;
import com.capgemini.cab.driversignupsigninservice.service.DriverService;

@RestController
@CrossOrigin("*")
public class DriverSignupSigninController {
	private String driverEmail;
	private List<Driver> driver;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DriverService service;

	@PostMapping("/registration")
	public ResponseEntity<Driver> registrationForDriver(@RequestBody Driver driver) {
		System.out.println(driver);
		Driver profile = service.addDetails(driver);
		System.out.println(profile);

		return new ResponseEntity<Driver>(profile, HttpStatus.CREATED);
	}

	@GetMapping("/login/{email}/{password}")
	public ResponseEntity<Driver> logInDetailsForUser(@PathVariable String email, @PathVariable String password)
			throws NullPointerException {

		Driver status = service.findByEmail(email);

		if ((status.getEmail().equals(email) && (status.getPassword().equals(password)) &&(status.getPass() != 0))) {

			return new ResponseEntity<Driver>(status, HttpStatus.ACCEPTED);
		}

		return new ResponseEntity<Driver>(HttpStatus.NOT_FOUND);

	}

	@GetMapping("/driverdetails")
	public ResponseEntity<DriverDetails> driverDetails() {
		DriverDetails details = new DriverDetails();
		List<Driver> finalDrivers=new ArrayList<Driver>();
		

		driver = new ArrayList<Driver>();

		driver = service.findAll();
		for (Driver driver2 : driver) {
			if(driver2.getPass()==0) {
				
				finalDrivers.add(driver2);
				
			}
			
		}
		details.setDriverDetails(finalDrivers);

		 
		
		// System.out.println(details.getDriverDetails());

		return new ResponseEntity<DriverDetails>(details,HttpStatus.ACCEPTED);
	}

	@GetMapping("/userdetails")
	public ResponseEntity<User> userDetails() {

		User user = restTemplate.getForEntity("http://USER-SIGNUP-SIGNIN/userdetailsfordriver", User.class).getBody();
		System.out.println(user.getUsername());
		return new ResponseEntity<User>(user, HttpStatus.FOUND);

	}

	@GetMapping("/userdetailsfordriver")
	public ResponseEntity<User> userDetailsForDriver() {

		// DriverDetails details = new DriverDetails();
		driver = new ArrayList<Driver>();
		driver = service.findAll();
		// System.out.println(driver);
		for (Driver driver2 : driver) {
			System.out.println(driver2);
			if (driver2.getStatus() == 0) {
				driverEmail = driver2.getEmail();
				driver2.setStatus(1);
				System.out.println(driver2);
				User u = restTemplate.getForEntity("http://USER-SIGNUP-SIGNIN/givinguserdetails", User.class).getBody();

				return new ResponseEntity<User>(u, HttpStatus.OK);

			}

		}

		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

	}

	@GetMapping("/pass/{email}")
	public ResponseEntity<Driver> passOfDriver(@PathVariable String email) {
		Driver d = service.findByEmail(email);
		d.setPass(1);
		System.out.println(d);
		return new ResponseEntity<Driver>(d, HttpStatus.OK);
	}

	/*
	 * @GetMapping("/checkingdriver") public ResponseEntity<Driver>
	 * userDetailsForDriverCheck(){ DriverDetails details = new DriverDetails();
	 * driver = new ArrayList<Driver>(); driver = service.findAll();
	 * System.out.println(driver); for (Driver driver2 : driver) { //
	 * System.out.println(driver2); if (driver2.getStatus() == 0) { return new
	 * ResponseEntity<Driver>(HttpStatus.NO_CONTENT); }
	 * 
	 * } return new ResponseEntity<Driver>(HttpStatus.NOT_ACCEPTABLE);
	 * 
	 * }
	 */

	@GetMapping("/userdetailsfordriver1")
	public ResponseEntity<Driver> userDetailsForDriver1() {

		DriverDetails details = new DriverDetails();
		driver = new ArrayList<Driver>();
		driver = service.findAll();
		System.out.println(driver);
		for (Driver driver2 : driver) {
			// System.out.println(driver2);
			if (driver2.getStatus() == 0) {
				driverEmail = driver2.getEmail();
				driver2.setStatus(1);
				System.out.println(driver2);
				User u = restTemplate.getForEntity("http://USER-SIGNUP-SIGNIN/givinguserdetails", User.class).getBody();

				return new ResponseEntity<Driver>(driver2, HttpStatus.OK);

			}

		}
		return new ResponseEntity<Driver>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/driveraccept")
	public ResponseEntity<Driver> driverdetailsForRiding() {

		driver = new ArrayList<Driver>();
		Driver d = service.findByEmail(driverEmail);
		d.setStatus(1);
		return new ResponseEntity<Driver>(d, HttpStatus.OK);

	}

	@GetMapping("/ridecomplete")
	public ResponseEntity<User> rideComplete() {
		Driver d = service.findByEmail(driverEmail);
		d.setStatus(0);
		System.out.println(d);

		return new ResponseEntity<User>(HttpStatus.OK);

	}

	@GetMapping("/fail/{email}")
	public ResponseEntity<Driver> deletingTheDriver(@PathVariable String email) {
		Driver fail = service.findByEmail(email);
		fail.setPass(0);
		System.out.println(fail);
		return new ResponseEntity<Driver>(HttpStatus.OK);

	}

}