package br.ce.wcaquino.taskbackend.controller;

import br.ce.wcaquino.taskbackend.model.Task;
import br.ce.wcaquino.taskbackend.repo.TaskRepo;
import br.ce.wcaquino.taskbackend.utils.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public class TaskControllerTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void naoDeveSalvarTarefaSemDescricao() {
        Task task = new Task();
        task.setDueDate(LocalDate.now());
        try {
            controller.save(task);
            Assert.fail("Não deveria salvar com sucesso");
        } catch (ValidationException e) {
            Assert.assertEquals("Fill the task description", e.getMessage());
        }
    }

    @Test
    public void naoDeveSalvarTarefaSemData() {
        Task task = new Task();
        task.setTask("Descrição");
        try {
            controller.save(task);
            Assert.fail("Não deveria salvar com sucesso");
        } catch (ValidationException e) {
            Assert.assertEquals("Fill the due date", e.getMessage());
        }
    }

    @Test
    public void naoDeveSalvarTarefaComDataPassada() {
        Task task = new Task();
        task.setTask("Descrição");
        task.setDueDate(LocalDate.of(2023, 1, 1));
        try {
            controller.save(task);
            Assert.fail("Não deveria salvar com sucesso");
        } catch (ValidationException e) {
            Assert.assertEquals("Due date must not be in past", e.getMessage());
        }
    }

    @Test
    public void deveSalvarTarefaComSucesso() throws ValidationException {
        Task task = new Task();
        task.setDueDate(LocalDate.now());
        task.setTask("Descrição");
        controller.save(task);
        Mockito.verify(taskRepo).save(task);
    }
}
