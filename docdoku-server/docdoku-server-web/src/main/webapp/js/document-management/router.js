define([
	"common/singleton_decorator",
	"views/folder_nav",
	"views/tag_nav",
	"views/workflow_nav",
	"views/template_nav",
	"views/checkedout_nav",
    "views/workflow_model_editor"
],
function (
	singletonDecorator,
	FolderNavView,
	TagNavView,
	WorkflowNavView,
	TemplateNavView,
	CheckedoutNavView,
    WorkflowModelEditorView
) {
	var Router = Backbone.Router.extend({
		routes: {
			"folders":			"folders",
			"folders/*path":	"folder",
			"tags":				"tags",
			"tags/:id": 		"tag",
			"templates":		"templates",
			"workflows":		"workflows",
            "workflow-model-editor":  "workflowModelEditor",
			"checkedouts":		"checkedouts",
			"tasks":			"tasks",
			"":					"defaults"
		},
		folders: function() {
			this.defaults();
			FolderNavView.getInstance().toggle();
		},
		folder: function(path) {
			this.defaults();
			FolderNavView.getInstance().show(path);
		},
		tags: function() {
			this.defaults();
			TagNavView.getInstance().toggle();
		},
		tag: function(id) {
			this.defaults();
			TagNavView.getInstance().show(id);
		},
		workflows: function() {
			this.defaults();
			var view = WorkflowNavView.getInstance();
			view.showContent();
		},
        workflowModelEditor: function() {
            this.defaults();
            var workflowModelEditorView = new WorkflowModelEditorView();
            workflowModelEditorView.render();
        },
		templates: function() {
			this.defaults();
			var view = TemplateNavView.getInstance();
			view.showContent();
		},
		checkedouts: function() {
			this.defaults();
			var view = CheckedoutNavView.getInstance();
			view.showContent();
		},
		tasks: function() {
			this.defaults();
		},
		defaults: function() {
			FolderNavView.getInstance();
			TagNavView.getInstance();
			WorkflowNavView.getInstance();
			TemplateNavView.getInstance();
			CheckedoutNavView.getInstance();
		}
	});
	Router = singletonDecorator(Router);
	return Router;
});
