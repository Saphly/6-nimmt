package com.nimmt.server;

import com.nimmt.server.model.Session;
import com.nimmt.server.model.SessionMessage;
import com.nimmt.server.model.SessionResponse;
import com.nimmt.server.service.SessionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.*;

import static com.nimmt.server.helper.JsonFileReader.fileToObjectList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestMongoConfig.class)
class ServerApplicationTests {

	@Autowired
	private SessionService sessionService;

	private List<Session> sessions = fileToObjectList();

	private Session testSession = new Session();

	private SessionResponse sessionResponse = new SessionResponse();

	private SessionMessage sessionMessage = new SessionMessage();

	@BeforeEach
	void repopulateCollection() {
		TestMongoConfig.clearCollection();
		TestMongoConfig.repopulateCollection(sessions);
	}

	@Nested
	@DisplayName("Get all sessions")
	class GetAllSessions {

		@Test
		@DisplayName("Should return a list of all the sessions")
		void shouldReturnListOfAllSessions() throws Exception {
			List<Session> result = sessionService.getAllSessions();

			assertThat(result, allOf(
						samePropertyValuesAs(sessions),
						hasSize(sessions.size())
			));
		}

		@Test
		@DisplayName("Should return empty list")
		void shouldReturnEmptyList() throws Exception {
			TestMongoConfig.clearCollection();
			List<Session> result = sessionService.getAllSessions();

			assertThat(result, hasSize(0));
		}
	}

	@Nested
	@DisplayName("Get session by ID")
	class GetSessionById {

		@Test
		@DisplayName("Should return the specific session given a valid ID")
		void shouldReturnSpecificSessionGivenValidId() throws Exception {
			Session result = sessionService.getSessionById(sessions.get(0).get_id())
									.orElseThrow();

			assertThat(result, samePropertyValuesAs(sessions.get(0)));
		}

		@Test
		@DisplayName("Should return empty given invalid ID")
		void shouldReturnEmptyGivenInvalidId() throws Exception {
			Optional<Session> result = sessionService.getSessionById("invalid-id");

			assertThat(result.isEmpty(), is(Boolean.TRUE));
		}
	}


	@Nested
	@DisplayName("Create session")
	class CreateSession {

		@BeforeEach
		void clearCollection() {
			TestMongoConfig.clearCollection();
		}

		@Test
		@DisplayName("Should return the created session")
		void shouldReturnCreatedSession() throws Exception {
			testSession.setName("TESTSession");
			Session result = sessionService.createSession(testSession);
			assertThat(result, hasProperty("name", is("TESTSession")));
		}
	}

	@Nested
	@DisplayName("Handle session message")
	class HandleSessionMessage {

		@Nested
		@DisplayName("JOIN request tests ")
		class JoinSessionTests {
			String playerId = "testUser1";

			@BeforeEach
			void setSessionMessageAction() {
				sessionMessage.setAction(SessionMessage.Action.JOIN);
			}

			@Test
			@DisplayName("Should return a message response of type JOINED with playerId if joining a session that is open,has less than max number of players, and session has updated list of players")
			void shouldReturnJoinedResponseWithPlayerId() throws Exception {
				Session testData = sessions.get(0);

				SessionResponse result = sessionService.handleSessionMessage(testData.get_id(), playerId,  sessionMessage);
				Session updatedSession = sessionService.getSessionById(testData.get_id()).orElseThrow();

				assertThat(result.getType(), is(SessionResponse.Type.JOINED));
				assertThat(result.getMessage(), is(playerId));
				assertTrue(updatedSession.getPlayers().contains(playerId));
			}

			@Test
			@DisplayName("Should return an IllegalStateException if joining a session that is closed")
			void shouldThrowIllegalStateExceptionIfSessionIsClosed() throws Exception {
				Session testData = sessions.get(1);

				Exception thrown = assertThrows(IllegalStateException.class,
						() -> sessionService.handleSessionMessage(testData.get_id(), playerId, sessionMessage)
				);

				assertEquals(thrown.getMessage(),"Session is no longer open");
			}

			@Test
			@DisplayName("Should return an IllegalStateException if joining a session that has max number of players already")
			void shouldThrowIllegalStateExceptionIfSessionHasMaxPlayers() throws Exception {
				Session testData = sessions.get(2);

				Exception thrown = assertThrows(IllegalStateException.class,
						() -> sessionService.handleSessionMessage(testData.get_id(), playerId, sessionMessage)
				);

				assertEquals(thrown.getMessage(),"Session is full");
			}

		}


		@Nested
		@DisplayName("LEAVE request tests")
		class LeaveSessionTest {

			@BeforeEach
			void setSessionMessageAction() {
				sessionMessage.setAction(SessionMessage.Action.LEAVE);
			}

			@Test
			@DisplayName("Should return a message response of type LEFT with playerId if leaving a session that is open, and updates player list")
			void shouldReturnLeftIfLeavingSessionThatIsOpenAndContainsPlayer() throws Exception {
				Session testData = sessions.get(0);
			 	String playerId = testData.getPlayers().stream().findFirst().get();

				SessionResponse result = sessionService.handleSessionMessage(testData.get_id(), playerId,  sessionMessage);
				Session updatedSession = sessionService.getSessionById(testData.get_id()).orElseThrow();

				assertThat(result.getType(), is(SessionResponse.Type.LEFT));
				assertThat(result.getMessage(), is(playerId));
				assertThat(updatedSession.getPlayers(), not(contains(playerId)));
			}

			@Test
			@DisplayName("Should throw IllegalStateException if leaving a session that is not open")
			void shouldThrowIllegalStateExceptionIfSessionIsNotOpen() throws Exception {
				Session testData = sessions.get(1);
				String playerId = testData.getPlayers().stream().findFirst().get();

				Exception thrown = assertThrows(IllegalStateException.class,
						() -> sessionService.handleSessionMessage(testData.get_id(), playerId, sessionMessage)
				);

				assertEquals(thrown.getMessage(),"Session is no longer open");
			}
		}

	}


}
