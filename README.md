# Application Model Playground for Vaadin

I've already played around with alternatives to Model-View-Presenter (MVP) in the 
[Cafe Tycoon](https://github.com/peholmst/cafetycoon) project. In this playground project, I decided to go "all the way"
with the Application Model (also called Presentation Model) pattern to see what it would look like in a Vaadin 
application.

## Basic idea

The user interface of the application consists of application models that contain the UI logic, and views that observe
the models and implement the actual UI. Application models expose actions and properties that can be bound to UI 
components such as menu items, buttons and input fields. A view normally only observes a single model, but the same
model can be observed by multiple views.

Application models can be arranged in hierarchies, were child models can inherit state and features from their parent
model. A parent model is, however, not aware of its children.

Models normally do not communicate with each other directly except with the parent model. A message broadcasting system
is in place which allows models to communicate in a decoupled way by broadcasting messages to all models in the 
hierarchy. It is also valid for models to observe and interact with each other directly, as long as the developer is
aware of the consequences this may have in terms of code complexity, coupling and reusability.
 
Models are aware of the backend / domain layer and interact directly with it. The models are smart - not just containers
of data.
 
Models are also aware of Vaadin and can use server push, the Navigator API, show notifications, etc. However, in my
framework there is a thin abstraction layer for this to make testing easier.
 
## Example code

I had to write some framework code which can be found in the [framework](src/main/java/org/vaadin/am4v/framework) 
package. The amount of framework code needed is also one of the downsides of this approach, indicating that this might 
not be a best practice, at least not without a supporting add-on.

The demo application that I wrote to try this out is a mail client with three different views: a tree with folders,
a table with messages in the selected folder and a view showing the currently selected message. The code can be 
found in the [demo](src/main/java/org/vaadin/am4v/demo) package. 

Please note that the UI is neatly split up into components and that the components themselves are fairly simple. Also
note that the entire UI could have been built without a single model. This means that you can start up the development
by building the UI, splitting it up into components as you see fit and then add the models afterwards - or leave them
out.

## Open questions

* Is this pattern still too complex to be useful in a real project?
* Could the amount of boiler plate code be reduced?
* Does it cover all common use cases?
* The way windows (dialogs) are handled needs a redesign. I'm not happy with it.
* How to handle cases where containers need to be shared between multiple views?
