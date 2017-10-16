package cat.udl.eps.softarch.mypadel.eventlistener;

import cat.udl.eps.softarch.mypadel.domain.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;

public class AdminEventListener extends AbstractRepositoryEventListener<Admin> {

	private final Logger logger = LoggerFactory.getLogger(Admin.class);

	@Override
	protected void onBeforeCreate(Admin entity) {
		super.onBeforeCreate(entity);
		logger.info("Before creating: {}", entity.toString());
	}

	@Override
	protected void onBeforeSave(Admin entity) {
		super.onBeforeSave(entity);
		logger.info("Before updating: {}", entity.toString());
	}

	@Override
	protected void onBeforeDelete(Admin entity) {
		super.onBeforeDelete(entity);
		logger.info("Before deleting: {}", entity.toString());
	}

	@Override
	protected void onBeforeLinkSave(Admin parent, Object linked) {
		super.onBeforeLinkSave(parent, linked);
		logger.info("Before linking: {} to {}", parent.toString(), linked.toString());
	}

	@Override
	protected void onAfterCreate(Admin entity) {
		super.onAfterCreate(entity);
		logger.info("After creating: {}", entity.toString());
	}

	@Override
	protected void onAfterSave(Admin entity) {
		super.onAfterSave(entity);
		logger.info("After updating: {}", entity.toString());
	}

	@Override
	protected void onAfterDelete(Admin entity) {
		super.onAfterDelete(entity);
		logger.info("After deleting: {}", entity.toString());
	}

	@Override
	protected void onAfterLinkSave(Admin parent, Object linked) {
		super.onAfterLinkSave(parent, linked);
		logger.info("After linking: {} to {}", parent.toString(), linked.toString());
	}

}
