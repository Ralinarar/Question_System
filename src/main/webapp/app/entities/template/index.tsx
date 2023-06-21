import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Template from './template';
import TemplateDetail from './template-detail';
import TemplateUpdate from './template-update';
import TemplateDeleteDialog from './template-delete-dialog';

const TemplateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Template />} />
    <Route path="new" element={<TemplateUpdate />} />
    <Route path=":id">
      <Route index element={<TemplateDetail />} />
      <Route path="edit" element={<TemplateUpdate />} />
      <Route path="delete" element={<TemplateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TemplateRoutes;
