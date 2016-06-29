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
 * A special binding that binds {@link ApplicationAction}s to {@link Action}s and also implements the
 * {@link com.vaadin.event.Action.Handler} interface so that it can be directly plugged into tables, trees, etc.
 * as a context menu. This binding works in a completely different way than {@link Binding} since context menus
 * are handled in a completely different way than other components in Vaadin.
 */
public class ActionHandlerBinding implements Action.Handler {

    private final List<Action> actionList = new ArrayList<>();
    private final Map<Action, ApplicationAction> actionToApplicationActionMap = new HashMap<>();

    /**
     * Adds a new {@link Action} to this binding. The action will have to be explicitly bound to an
     * {@link ApplicationAction} using {@link #bind(Action, ApplicationAction)}.
     * 
     * @param caption the caption of the action.
     * @param icon the icon of the action.
     * @return the created action.
     */
    public Action add(String caption, Resource icon) {
        Action action = new Action(caption, icon);
        actionList.add(action);
        return action;
    }

    /**
     * Adds a new {@link Action} to this binding. The action will have to be explicitly bound to an
     * {@link ApplicationAction} using {@link #bind(Action, ApplicationAction)}.
     *
     * @param caption the caption of the action.
     * @return the created action.
     */
    public Action add(String caption) {
        return add(caption, null);
    }

    /**
     * Removes the specified action from this binding. If the action did not exist in the first place, nothing happens.
     * 
     * @param action the action to remove.
     */
    public void remove(Action action) {
        actionList.remove(action);
        actionToApplicationActionMap.remove(action);
    }

    /**
     * Binds the specified {@code action} to the specified {@code applicationAction}.
     *
     * @see #unbind(Action)
     * 
     * @param action the action.
     * @param applicationAction the application action.
     * @throws IllegalStateException if the action does not exist or has already been bound to an ApplicationAction.
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
     * Unbinds the specified action from its application action. If the action has not been bound in the first place,
     * nothing happens.
     *
     * @param action the action to unbind.
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
