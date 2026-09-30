export type SourceConnection = {
  id: string;
  title: string;
  type: string;
  workspace: string;
  status: 'active' | 'syncing' | 'error';
};

export type ResearchResponse = {
  question: string;
  answer: string;
  citations: Array<{
    id: string;
    label: string;
    sourceId: string;
  }>;
  searchedSources: number;
  time: string;
};

export type SourceListResponse = {
  items: SourceConnection[];
};
