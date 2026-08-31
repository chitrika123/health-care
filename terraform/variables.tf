variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-2"
}

variable "key_name" {
  description = "EC2 key pair"
  type        = string
  default     = "projectkey"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.small"
}
