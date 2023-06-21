import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Predicate from './predicate';
import PredicateDetail from './predicate-detail';
import PredicateUpdate from './predicate-update';
import PredicateDeleteDialog from './predicate-delete-dialog';

const PredicateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Predicate />} />
    <Route path="new" element={<PredicateUpdate />} />
    <Route path=":id">
      <Route index element={<PredicateDetail />} />
      <Route path="edit" element={<PredicateUpdate />} />
      <Route path="delete" element={<PredicateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PredicateRoutes;
