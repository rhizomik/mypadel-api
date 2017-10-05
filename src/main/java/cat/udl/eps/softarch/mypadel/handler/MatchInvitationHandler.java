package cat.udl.eps.softarch.mypadel.handler;

import cat.udl.eps.softarch.mypadel.domain.MatchInvitation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Calendar;


public class MatchInvitationHandler {
    final Logger logger = LoggerFactory.getLogger(MatchInvitation.class);

    @HandleBeforeCreate
    @Transactional
    public void handleMatchInvitationPreCreate(MatchInvitation match) {
        logger.info("Before creating: {}", match.toString());
        match.setEventDate(ZonedDateTime.now());
    }

 /* @HandleBeforeSave
    @Transactional
    public void handleAdminPreSave(Admin admin){
        logger.info("Before updating: {}", admin.toString());
    }

    @HandleBeforeDelete
    @Transactional
    public void handleAdminPreDelete(Admin admin){
        logger.info("Before deleting: {}", admin.toString());
    }

    @HandleBeforeLinkSave
    public void handleAdminPreLinkSave(Admin admin, Object o) {
        logger.info("Before linking: {} to {}", admin.toString(), o.toString());
    }

    @HandleAfterCreate
    @Transactional
    public void handleAdminPostCreate(Admin admin){
        logger.info("After creating: {}", admin.toString());
    }

    @HandleAfterSave
    @Transactional
    public void handleAdminPostSave(Admin admin){
        logger.info("After updating: {}", admin.toString());
    }

    @HandleAfterDelete
    @Transactional
    public void handleAdminPostDelete(Admin admin){
        logger.info("After deleting: {}", admin.toString());
    }

    @HandleAfterLinkSave
    public void handleAdminPostLinkSave(Admin admin, Object o) {
        logger.info("After linking: {} to {}", admin.toString(), o.toString());
    }*/
}
