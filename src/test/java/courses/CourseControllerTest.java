package courses;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

public class CourseControllerTest {

	// Mock Objects represent the database populator for tests

	@InjectMocks
	private CourseController underTest;

	@Mock
	private Course course;
	Long courseId;

	@Mock
	private Course anotherCourse;

	@Mock
	private Textbook textbook;

	@Mock
	private Textbook anotherTextbook;

	@Mock
	private CourseRepository courseRepo;

	@Mock
	private TopicRepository topicRepo;

	@Mock
	private TextbookRepository textbookRepo;

	@Mock
	private Model model;

	@Mock
	private Topic topic;

	@Mock
	private Topic anotherTopic;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldAddSingleCourseToModel() throws CourseNotFoundException {
		long arbitraryCourseId = 1;
		when(courseRepo.findById(arbitraryCourseId)).thenReturn(Optional.of(course));

		underTest.findOneCourse(arbitraryCourseId, model);
		verify(model).addAttribute("courses", course);
	}

	@Test
	public void shouldAddAllCoursesToModel() {
		Collection<Course> allCourses = Arrays.asList(course, anotherCourse);
		when(courseRepo.findAll()).thenReturn(allCourses);

		underTest.findAllCourses(model);
		verify(model).addAttribute("courses", allCourses);
	}

	@Test
	public void shouldAddSingleTopicToModel() throws TopicNotFoundException {
		long arbitraryTopicId = 1;
		when(topicRepo.findById(arbitraryTopicId)).thenReturn(Optional.of(topic));

		underTest.findOneTopic(arbitraryTopicId, model);

		verify(model).addAttribute("topics", topic);
	}

	@Test
	public void shouldAddAllTopicsToModel() {
		Collection<Topic> allTopics = Arrays.asList(topic, anotherTopic);
		when(topicRepo.findAll()).thenReturn(allTopics);

		underTest.findAllTopics(model);
		verify(model).addAttribute("topics", allTopics);
	}

	@Test
	public void shouldAddSingleTextbookToModel() throws TextbookNotFoundException {
		long arbitraryTextbookId = 1;
		when(textbookRepo.findById(arbitraryTextbookId)).thenReturn(Optional.of(textbook));

		underTest.findOneTextbook(arbitraryTextbookId, model);

		verify(model).addAttribute("textbooks", textbook);
	}

	@Test
	public void shouldAddAllTextbooksToModel() {
		Collection<Textbook> allTextbooks = Arrays.asList(textbook, anotherTextbook);
		when(textbookRepo.findAll()).thenReturn(allTextbooks);

		underTest.findAllTextbooks(model);
		verify(model).addAttribute("textbooks", allTextbooks);
	}

	@Test // User input will create new course - drive controller method for user to add
			// courses
	public void shouldAddAdditionalCoursesToModel() {
		// Create an arbitrary new course for the test (naming needs to match Course
		// Object)
		String topicName = "topic name";
		// Need to check to see if new topic exists already
		Topic newTopic = topicRepo.findByName(topicName);
		String courseName = "new course";
		String courseDescription = "new course description";
		// underTest is calling the controller that was injected at the top of the page
		// and called underTest
		underTest.addCourse(courseName, courseDescription, topicName);
		// create the newCourse object
		Course newCourse = new Course(courseName, courseDescription, newTopic);
		when(courseRepo.save(newCourse)).thenReturn(newCourse);
	}

	@Test
	public void shouldRemoveCourseFromModelByName() {
		String courseName = course.getName();
		when(courseRepo.findByName(courseName)).thenReturn(course); // referencing the Mocked course, above
		underTest.deleteCourseByName(courseName);
		verify(courseRepo).delete(course);
	}

	@Test
	public void shouldRemoveCourseFromModelById() {
		underTest.deleteCourseById(courseId);
		verify(courseRepo).deleteById(courseId);
	}

	@Test
	public void shouldAddAdditionalTextbooksToModel() {
		String courseName = "name";
		String title = "title";
		when(course.getName()).thenReturn(courseName);
		when(courseRepo.findByName(courseName)).thenReturn(course);
		underTest.addTextbook(title, courseName);
		verify(textbookRepo).save(new Textbook(title, course));

	}
}