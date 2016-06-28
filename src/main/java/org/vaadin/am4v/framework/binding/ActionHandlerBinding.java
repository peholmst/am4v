package org.vaadin.am4v.framework.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.vaadin.am4v.framework.model.ApplicationAction;
import org.vaadin.am4v.framework.model.ContextualApplicationAction;

import com.vaadin.event.Action;
import com.vaadin.server.Resource;

/**
 *
 */
public class ActionHandlerBinding implements Action.Handler {

    private final List<Action> actionList = new ArrayList<>();
    private final Map<Action, ApplicationAction> actionToApplicationActionMap = new HashMap<>();

    /**
     * 
     * @param caption
     * @param icon
     * @return
     */
    public Action add(String caption, Resource icon) {
        Action action = new Action(caption, icon);
        actionList.add(action);
        return action;
    }

    /**
     * 
     * @param caption
     * @return
     */
    public Action add(String caption) {
        return add(caption, null);
    }

    /**
     *
     * @param action
     */
    public void remove(Action action) {
        actionList.remove(action);
        actionToApplicationActionMap.remove(action);
    }

    /**
     *
     * @param action
     * @param applicationAction
     */
    public void bind(Action action, ApplicationAction applicationAction) {
        if (actionList.contains(action)) {
            if (actionToApplicationActionMap.containsKey(action)) {
                throw new IllegalStateException("The Action is already bound to an ApplicationAction");
            }
            actionToApplicationActionMap.put(action, applicationAction);
        } else {
            throw new IllegalStateException("No such Action found");
        }
    }

    /**
     *
     * @param action
     */
    public void unbind(Action action) {
        actionToApplicationActionMap.remove(action);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        List<Action> availableActions = actionList.stream().filter(a -> isActionAvailable(a, target))
            .collect(Collectors.toList());
        return availableActions.toArray(new Action[availableActions.size()]);
    }

    @SuppressWarnings("unchecked")
    private boolean isActionAvailable(Action action, Object target) {
        ApplicationAction applicationAction = actionToApplicationActionMap.get(action);
        if (applicationAction == null) {
            return false;
        } else if (applicationAction instanceof ContextualApplicationAction) {
            return ((ContextualApplicationAction) applicationAction).isEnabled(target)
                && ((ContextualApplicationAction) applicationAction).isVisible(target);
        } else {
            return applicationAction.isEnabled() && applicationAction.isVisible();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleAction(Action action, Object sender, Object target) {
        ApplicationAction applicationAction = actionToApplicationActionMap.get(action);
        if (applicationAction == null) {
            throw new IllegalStateException("No ApplicationAction found for action");
        }
        if (applicationAction instanceof ContextualApplicationAction) {
            ((ContextualApplicationAction) applicationAction).run(target);
        } else {
            applicationAction.run();
        }
    }
}
