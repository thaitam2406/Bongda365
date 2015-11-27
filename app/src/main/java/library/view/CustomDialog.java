package library.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class CustomDialog extends Dialog {
	Context mContext;
	public CustomDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	public CustomDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private View separatorView;

		private OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @return
		 */
		public Builder setMessage(String message) {
			try {
				this.message = message;
			} catch (Exception e) {
				// TODO: handle exception
			}
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @return
		 */
		public Builder setMessage(int message) {
			try {
				this.message = (String) context.getText(message);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			try {
				this.title = (String) context.getText(title);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			try {
				this.title = title;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			try {
				this.positiveButtonText = (String) context
						.getText(positiveButtonText);
				this.positiveButtonClickListener = listener;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			try {
				this.positiveButtonText = positiveButtonText;
				this.positiveButtonClickListener = listener;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			try {
				this.negativeButtonText = (String) context
						.getText(negativeButtonText);
				this.negativeButtonClickListener = listener;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			try {
				this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return this;
		}
		public void hideSaperatorView(){
			separatorView.setVisibility(View.GONE);
		}
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            /*final CustomDialog dialog = new CustomDialog(context, Util.getIdResourcebyTheme(context, "dialog_style", "attr"));
            View layout = inflater.inflate(Util.getIdXml("dialog_custom", "layout"), null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            try {
            	((TextView) layout.findViewById(Util.getIdXml("title", "id"))).setText(title);
			} catch (Exception e) {
				// TODO: handle exception
			}
            separatorView = ((View)layout.findViewById(Util.getIdXml("separator", "id")));
            // set the confirm button
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(Util.getIdXml("positiveButton", "id")))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((TextView) layout.findViewById(Util.getIdXml("positiveButton", "id")))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(Util.getIdXml("positiveButton", "id")).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(Util.getIdXml("negativeButton", "id")))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((TextView) layout.findViewById(Util.getIdXml("negativeButton", "id")))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(Util.getIdXml("negativeButton", "id")).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(Util.getIdXml("message", "id"))).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(Util.getIdXml("content", "id")))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(Util.getIdXml("content", "id")))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.WRAP_CONTENT, 
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);*/
            return null;
			
		}

		
	}
	
	

}

