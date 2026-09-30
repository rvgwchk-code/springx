import cors from 'cors';
import express from 'express';
import { z } from 'zod';
import { config } from './config.js';
import type { ResearchResponse, SourceListResponse } from './types.js';

const app = express();
app.use(cors());
app.use(express.json());

const researchQuerySchema = z.object({
  question: z.string().min(1).max(200),
});

const researchAnswer: ResearchResponse = {
  question: 'What changed in our customer onboarding?',
  answer:
    'Onboarding shifted from a single handoff to a staged, owner-led journey. The current playbook adds a kickoff within two business days, a named implementation lead, and a readiness check before data import begins.',
  citations: [
    { id: '1', label: 'Customer onboarding playbook', sourceId: 'source-1' },
    { id: '2', label: 'Q2 onboarding retrospective', sourceId: 'source-2' },
    { id: '3', label: 'Enterprise implementation checklist', sourceId: 'source-3' },
  ],
  searchedSources: 3,
  time: '0.9s',
};

const sourceList: SourceListResponse = {
  items: [
    { id: 'source-1', title: 'Customer onboarding playbook', type: 'Notion', workspace: 'Product Ops', status: 'active' },
    { id: 'source-2', title: 'Q2 onboarding retrospective', type: 'Google Drive', workspace: 'CX', status: 'syncing' },
    { id: 'source-3', title: 'Enterprise implementation checklist', type: 'SharePoint', workspace: 'Platform', status: 'active' },
  ],
};

app.get('/api/health', (_req, res) => {
  res.json({ ok: true, service: 'api', environment: config.nodeEnv });
});

app.get('/api/research', (req, res) => {
  const parsed = researchQuerySchema.safeParse(req.query);

  if (!parsed.success) {
    return res.status(400).json({
      error: {
        code: 'VALIDATION_ERROR',
        message: 'A valid question is required.',
        details: parsed.error.flatten(),
      },
    });
  }

  return res.json({
    ...researchAnswer,
    question: parsed.data.question || researchAnswer.question,
  });
});

app.get('/api/sources', (_req, res) => {
  res.json(sourceList);
});

app.listen(config.port, () => {
  console.log(`API listening on http://localhost:${config.port}`);
});
