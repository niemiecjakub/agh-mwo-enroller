package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Comparator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll(String sortBy, String sortOrder, String key) {
		//Sortowac w bazie - za pomoca hql - a nie na serverze juz,
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		Collection<Participant> participants = query.list();

		if (!key.isEmpty()) {
			participants = participants.stream().filter(p -> p.getLogin().contains(key)).toList();
		}

		if (sortOrder.equals("ASC")) {
			participants = participants.stream().sorted(Comparator.comparing(Participant::getLogin)).toList();
		}

		if (sortOrder.equals("DESC")) {
			participants = participants.stream().sorted(Comparator.comparing(Participant::getLogin).reversed()).toList();
		}

		return participants;
	}

	public Participant getByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public void register(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public void update(Participant updateParticipant) {
		Transaction transaction = connector.getSession().beginTransaction();
		Participant participant = connector.getSession().find(Participant.class, updateParticipant.getLogin());
		participant.setPassword(updateParticipant.getPassword());
		transaction.commit();
	}
}
