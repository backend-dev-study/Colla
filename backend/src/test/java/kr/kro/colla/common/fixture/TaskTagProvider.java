package kr.kro.colla.common.fixture;

import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_tag.domain.TaskTag;

public class TaskTagProvider {

    public static TaskTag createTaskTag(Task task, Tag tag) {
        return new TaskTag(task, tag);
    }

}
