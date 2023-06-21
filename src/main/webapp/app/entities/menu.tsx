import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/template">
        <Translate contentKey="global.menu.entities.template" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/predicate">
        <Translate contentKey="global.menu.entities.predicate" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mathtest">
        <Translate contentKey="global.menu.entities.mathtest" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/question">
        <Translate contentKey="global.menu.entities.question" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
