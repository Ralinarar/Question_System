import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IMathtest } from 'app/shared/model/mathtest.model';
import { getEntity, updateEntity, createEntity, reset } from './mathtest.reducer';

export const MathtestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const mathtestEntity = useAppSelector(state => state.mathtest.entity);
  const loading = useAppSelector(state => state.mathtest.loading);
  const updating = useAppSelector(state => state.mathtest.updating);
  const updateSuccess = useAppSelector(state => state.mathtest.updateSuccess);

  const handleClose = () => {
    navigate('/mathtest' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...mathtestEntity,
      ...values,
      assigneds: mapIdList(values.assigneds),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...mathtestEntity,
          assigneds: mathtestEntity?.assigneds?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="qsApp.mathtest.home.createOrEditLabel" data-cy="MathtestCreateUpdateHeading">
            <Translate contentKey="qsApp.mathtest.home.createOrEditLabel">Create or edit a Mathtest</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="mathtest-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('qsApp.mathtest.amount')} id="mathtest-amount" name="amount" data-cy="amount" type="text" />
              <ValidatedField label={translate('qsApp.mathtest.keys')} id="mathtest-keys" name="keys" data-cy="keys" type="text" />
              <ValidatedField
                label={translate('qsApp.mathtest.treshold')}
                id="mathtest-treshold"
                name="treshold"
                data-cy="treshold"
                type="text"
              />
              <ValidatedField
                label={translate('qsApp.mathtest.assigned')}
                id="mathtest-assigned"
                data-cy="assigned"
                type="select"
                multiple
                name="assigneds"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mathtest" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MathtestUpdate;
