package com.mysoft.pims.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mysoft.pims.controller.storage.service.FileStorageService;
import com.mysoft.pims.model.Patient;
import com.mysoft.pims.repository.PatientRepo;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

	@Autowired
	private PatientRepo repo;

	@Autowired
	private FileStorageService fileStorageService;

//	@PostMapping("/patientAdd")
//	public ResponseEntity<?> save(@ModelAttribute Patient entity ) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//
//			Patient patient = repo.save(entity);
//			map.put("message", "Patient successfully saved");
//			map.put("data", patient);
//			map.put("statusCode", 200);
//			return ResponseEntity.ok(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			map.put("message", "Patient saved failed");
//			map.put("data", null);
//			map.put("statusCode", 400);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
//		}
//	}
	
	@PostMapping("/patientAdd")
	public ResponseEntity<?> save(@ModelAttribute Patient entity, @RequestParam("file") MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		try {

			String fileName = fileStorageService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileName).toUriString();

			entity.setPhotos(fileName);
			entity.setPhotosUri(fileDownloadUri);

			Patient patient = repo.save(entity);
			map.put("message", "Patient successfully saved");
			map.put("data", patient);
			map.put("statusCode", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Patient saved failed");
			map.put("data", null);
			map.put("statusCode", 400);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

	
	@GetMapping(value = "/getPatient/{id}")
	public ResponseEntity<?> getPatient(@PathVariable(value = "id") Integer id) {
		Map<String, Object> map = new HashMap<>();
		Patient patient = repo.findById(id).get();
		try {
			map.put("message", "Patient deleted successfully");
			map.put("data", patient);
			map.put("statusCode", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Patient deletation failed");
			map.put("data", null);
			map.put("statusCode", 400);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}
	
	@GetMapping("/getAllPatient")
	public ResponseEntity<?> getPatient() {
		Map<String, Object> map = new HashMap<>();
		try {
			List<Patient> patient = (List<Patient>) repo.findAll();
			map.put("message", "Patient get successfully");
			map.put("data", patient);
			map.put("statusCode", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Patient fetch failed");
			map.put("data", null);
			map.put("statusCode", 400);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}
	
	
	
	@PostMapping("/patientUpdate")
	public ResponseEntity<?> update(@ModelAttribute Patient entity) {
		Map<String, Object> map = new HashMap<>();
	System.out.println(entity.getId());
		try {
			Patient patient = repo.save(entity);
						map.put("message", "Patient updated successfully");
			map.put("data", patient);
			map.put("statusCode", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Patient updated failed");
			map.put("data", null);
			map.put("statusCode", 400);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

	@GetMapping(value = "/deletePatient/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
		Map<String, Object> map = new HashMap<>();
		Patient patient = repo.findById(id).get();
		try {
			repo.delete(patient);
			map.put("message", "Patient deleted successfully");
			map.put("data", patient);
			map.put("statusCode", 200);
			return ResponseEntity.ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", "Patient deletation failed");
			map.put("data", null);
			map.put("statusCode", 400);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
		}
	}

}
