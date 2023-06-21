import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mathtest.reducer';

export const MathtestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mathtestEntity = useAppSelector(state => state.mathtest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mathtestDetailsHeading">
          <Translate contentKey="qsApp.mathtest.detail.title">Mathtest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mathtestEntity.id}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="qsApp.mathtest.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{mathtestEntity.amount}</dd>
          <dt>
            <span id="keys">
              <Translate contentKey="qsApp.mathtest.keys">Keys</Translate>
            </span>
          </dt>
          <dd>{mathtestEntity.keys}</dd>
          <dt>
            <span id="treshold">
              <Translate contentKey="qsApp.mathtest.treshold">Treshold</Translate>
            </span>
          </dt>
          <dd>{mathtestEntity.treshold}</dd>
          <dt>
            <Translate contentKey="qsApp.mathtest.assigned">Assigned</Translate>
          </dt>
          <dd>
            {mathtestEntity.assigneds
              ? mathtestEntity.assigneds.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.login}</a>
                    {mathtestEntity.assigneds && i === mathtestEntity.assigneds.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/mathtest" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mathtest/${mathtestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MathtestDetail;
