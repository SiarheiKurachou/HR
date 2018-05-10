package by.molchanov.humanresources.command.impl;

import by.molchanov.humanresources.command.ConcreteCommand;
import by.molchanov.humanresources.controller.RequestHolder;
import by.molchanov.humanresources.dto.JobRequestDTO;
import by.molchanov.humanresources.entity.User;
import by.molchanov.humanresources.exception.CustomBrokerException;
import by.molchanov.humanresources.exception.CustomExecutorException;
import by.molchanov.humanresources.executor.RegistrationExecutor;
import by.molchanov.humanresources.executor.impl.RegistrationExecutorImpl;

import static by.molchanov.humanresources.constant.SessionRequestAttributeNames.*;

public class JobRequestRegistrationImpl implements ConcreteCommand {
    private static final RegistrationExecutor REGISTRATION_EXECUTOR = RegistrationExecutorImpl.getInstance();
    private static final ConcreteCommand FILL_VACANCY_COMMAND = FillVacancyCommandImpl.getInstance();

    private static final int FIRST_INDEX = 0;

    @Override
    public void execute(RequestHolder requestHolder) throws CustomBrokerException {
        String resume = requestHolder.getSingleRequestParameter(FIRST_INDEX, REQUEST_RESUME);
        int vacancyId = Integer.parseInt(requestHolder.getSingleRequestParameter(FIRST_INDEX, VACANCY_ID));
        User user = (User) requestHolder.getSessionAttribute(USER_INFO);
        int userId = user.getId();
        JobRequestDTO jobRequestDTO = new JobRequestDTO();
        jobRequestDTO.setResume(resume);
        jobRequestDTO.setUserId(userId);
        jobRequestDTO.setVacancyId(vacancyId);
        try {
            REGISTRATION_EXECUTOR.requestSignUp(jobRequestDTO);
            FILL_VACANCY_COMMAND.execute(requestHolder);
        } catch (CustomExecutorException e) {
            throw new CustomBrokerException(e);
        }
        requestHolder.addRequestAttribute(INFO_MESSAGE, jobRequestDTO.getInfoMessage());
    }
}
