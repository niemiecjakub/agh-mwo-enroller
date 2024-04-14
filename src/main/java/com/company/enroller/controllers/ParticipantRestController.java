package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(
			@RequestParam(value = "sortBy", defaultValue = "login") String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder,
			@RequestParam(value = "key", defaultValue = "") String key) {
		Collection<Participant> participants = participantService.getAll(sortBy, sortOrder, key);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.getByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant existingParticipant = participantService.getByLogin(participant.getLogin());
		if (existingParticipant != null) {
			return new ResponseEntity<>("User with this login already exists",HttpStatus.CONFLICT);
		}
		participantService.register(participant);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.getByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant updateParticipant) {
		Participant participant = participantService.getByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
		}
		participantService.update(updateParticipant);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
