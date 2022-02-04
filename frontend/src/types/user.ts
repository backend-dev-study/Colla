export interface UserProfile {
    githubId: string;
    avatar: string;
    displayName: string;
}

export interface NoticeType {
    id: number;
    noticeType: string;
    isChecked: boolean;
    mentionedURL?: string;
    projectId?: number;
    projectName?: string;
}
