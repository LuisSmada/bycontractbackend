package com.beyond.bycontract.template.application.service;

import com.beyond.bycontract.template.application.dto.CreateTemplateCommand;
import com.beyond.bycontract.template.application.dto.FindTemplateResponse;
import com.beyond.bycontract.template.application.dto.TemplateResponse;
import com.beyond.bycontract.template.domain.exception.TemplateNotFoundException;
import com.beyond.bycontract.template.domain.model.Template;
import com.beyond.bycontract.template.domain.repository.TemplateRepository;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {

	private final TemplateRepository repository;
	private final UserRepository userRepository;

	private String getFullName(User user) {
		return user.getFirstName() + " " + user.getLastName();
	}

	public TemplateResponse createTemplate(CreateTemplateCommand command) {

		Template template = Template.create(
				command.name(),
				command.body(),
				command.idAuthor()
		);

		Template savedTemplate = repository.createTemplate(template);
		User userFound = userRepository.getUserById(savedTemplate.getIdAuthor()).orElseThrow(() -> new UsernameNotFoundException("User with id: " + savedTemplate.getIdAuthor() + " not found"));

		return new TemplateResponse(savedTemplate.getId(), savedTemplate.getName(), userFound.getFirstName() + " " + userFound.getLastName(), template.getCreatedAt(), template.getModifiedAt());
	}

	public List<TemplateResponse> getAllTemplates() {
		List<Template> allTemplates = repository.getAllTemplates();

		Set<UUID> idAuthors = allTemplates.stream().map(Template::getIdAuthor).collect(Collectors.toSet());
		List<User> authors = userRepository.getUsersByIds(idAuthors);

		Map<UUID, User> authorsById = authors.stream().collect(Collectors.toMap(
				User::getId,
				Function.identity()
		));

		return allTemplates.stream().map(template -> {

					User author = Optional.ofNullable(authorsById.get(template.getIdAuthor()))
							.orElseThrow(() -> new UsernameNotFoundException(
									"User with id: " + template.getIdAuthor() + " not found"
							));

					return new TemplateResponse(
							template.getId(),
							template.getName(),
							getFullName(author),
							template.getCreatedAt(),
							template.getModifiedAt()
					);

				}
		).toList();
	}

	public FindTemplateResponse findTemplateById(UUID id) {
		Template template = repository
				.findTemplateById(id)
				.orElseThrow(() -> new TemplateNotFoundException(id));

		User author = userRepository
				.getUserById(template.getIdAuthor())
				.orElseThrow(() -> new UserNotFoundException(
						template.getIdAuthor()
				));

		return new FindTemplateResponse(
				template.getId(),
				template.getName(),
				getFullName(author),
				template.getBody(),
				template.getVariablesDefinition(),
				template.getCreatedAt(),
				template.getModifiedAt()
		);
	}
}
