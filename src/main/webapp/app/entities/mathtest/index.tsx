import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mathtest from './mathtest';
import MathtestDetail from './mathtest-detail';
import MathtestUpdate from './mathtest-update';
import MathtestDeleteDialog from './mathtest-delete-dialog';
import QuizComponent from "app/entities/mathtest/mathtest-quiz";

const MathtestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mathtest />} />
    <Route path="new" element={<MathtestUpdate />} />
    <Route path=":id">
      <Route index element={<MathtestDetail />} />
      <Route path="edit" element={<MathtestUpdate />} />
      <Route path="delete" element={<MathtestDeleteDialog />} />
      <Route path="quiz" element={<QuizComponent />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MathtestRoutes;
