package kr.kro.colla.meeting_place.meeting_place.domain.repository;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class MeetingPlaceRepositoryTest {

    @Autowired
    private MeetingPlaceRepository meetingPlaceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void 프로젝트의_모임_장소를_생성한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(324091L));
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace(project);

        // when
        MeetingPlace result = meetingPlaceRepository.save(meetingPlace);

        // then
        assertThat(result.getName()).isEqualTo(meetingPlace.getName());
        assertThat(result.getLongitude()).isEqualTo(meetingPlace.getLongitude());
        assertThat(result.getLatitude()).isEqualTo(meetingPlace.getLatitude());
        assertThat(result.getAddress()).isEqualTo(meetingPlace.getAddress());
        assertThat(result.getProject().getId()).isEqualTo(project.getId());
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
