package kr.kro.colla.meeting_place.meeting_place.domain.repository;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MeetingPlaceRepositoryTest {

    @Autowired
    MeetingPlaceRepository meetingPlaceRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void 프로젝트의_모임_장소를_생성한다() throws Exception {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(324091L));

        // when
        MeetingPlace meetingPlace = meetingPlaceRepository.save(MeetingPlaceProvider.createMeetingPlace());
        project.addMeetingPlace(meetingPlace);

        testEntityManager.flush();
        testEntityManager.clear();

        // then
        meetingPlaceRepository.findById(meetingPlace.getId()).orElseThrow(Exception::new);
        assertThat(project.getMeetingPlaces().size()).isEqualTo(1);
    }

    @Test
    void 모임_장소의_필수_필드를_부족시_생성할_수_없다() {
        // given
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .build();

        // when, then
        assertThatThrownBy(() -> {
            meetingPlaceRepository.save(meetingPlace);
        }).isInstanceOf(ConstraintViolationException.class);
    }
}